package com.school.service.classschedule;

import com.school.model.dto.sclassschedule.TimeSlotDTO;
import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.ClassRoom;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.service.ClassRoomService;
import com.school.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.school.service.classschedule.AdjacentTimeSlotsUtils.adjacentTimeSlotIsPresent;
import static com.school.service.classschedule.AdjacentTimeSlotsUtils.findPreviousOrNextTimeSlotWithMatchingSubject;

@Service
public class TimeSlotManagingService {
    private final TimeSlotQueryService timeSlotQueryService;
    private final SubjectService subjectService;
    private final ClassRoomService classRoomService;
    private static final Logger log = LoggerFactory.getLogger(TimeSlotManagingService.class);

    public TimeSlotManagingService(TimeSlotQueryService timeSlotQueryService, SubjectService subjectService, ClassRoomService classRoomService) {
        this.timeSlotQueryService = timeSlotQueryService;
        this.subjectService = subjectService;
        this.classRoomService = classRoomService;
    }

    public TimeSlotDTO assignSubjectToTimeSlot(Long subjectId, Long timeSlotId) {
        try {
            List<ClassRoom> unassignedClassRooms = classRoomService.getUnassignedClassRooms();
            if (unassignedClassRooms.isEmpty()) {
                throw new IllegalStateException("No empty class rooms available, consider adding new class rooms!");
            } else {
                log.debug("subjectId to be added: {} timeSlotId to be taken: {}", subjectId, timeSlotId);
                Subject currentSubject = subjectService.getSubjectById(subjectId);
                TimeSlot currentTimeSlot = timeSlotQueryService.getTimeSlotById(timeSlotId);
                List<TimeSlot> currentScheduleEntryTimeSlots = timeSlotQueryService.getTimeSlotsByScheduleEntry(currentTimeSlot.getScheduleEntry());

                ClassRoom unassignedClassRoom;
                TimeSlot adjacentTimeSlot = findPreviousOrNextTimeSlotWithMatchingSubject(
                        currentScheduleEntryTimeSlots,
                        currentTimeSlot,
                        currentSubject
                );
                if (adjacentTimeSlotIsPresent(adjacentTimeSlot)) {
                    unassignedClassRoom = getMatchingSubjectClassRoom(adjacentTimeSlot);
                } else {
                    unassignedClassRoom = getRandomUnassignedClassRoom(unassignedClassRooms);
                }
                return new TimeSlotDTO(saveTimeSlotWithAssignedSubjectAndClassRoom(currentTimeSlot, currentSubject, unassignedClassRoom));
            }
        } catch (Exception e) {
            log.error("Error occurred while assigning subject to time slot: ", e);
            throw new IllegalStateException(e);
        }
    }

    private ClassRoom getMatchingSubjectClassRoom(TimeSlot currentTimeSlot) {

        return currentTimeSlot.getClassRoom();
    }

    private ClassRoom getRandomUnassignedClassRoom(List<ClassRoom> unassignedClassRooms) {
        log.info("Timeslot has no previous or next slots taken, thus returning class room randomly");
        Collections.shuffle(unassignedClassRooms);
        return unassignedClassRooms.get(0);
    }

    private TimeSlot saveTimeSlotWithAssignedSubjectAndClassRoom(TimeSlot timeSlot, Subject subject, ClassRoom classRoom) {
        TimeSlot updatedTimeSlot = timeSlotQueryService.assignSubjectAndClassRoomToTimeSlot(timeSlot, subject, classRoom);
        classRoom.assignTimeSlot(timeSlot);
        classRoomService.updateWithAssignedTimeSlot(classRoom, timeSlot);
        log.info("subject id: {} added to time slot id: {} and assigned class room no: {}",
                updatedTimeSlot.getSubject().getId(),
                updatedTimeSlot.getId(),
                updatedTimeSlot.getClassRoom().getRoomNumber()
        );
        return updatedTimeSlot;
    }
}