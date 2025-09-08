package com.school.service.classschedule;

import com.school.configuration.ClassScheduleConfig;
import com.school.model.dto.sclassschedule.TimeSlotDTO;
import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.ClassRoom;
import com.school.model.entity.classschedule.ClassSchedule;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.service.ClassRoomService;
import com.school.service.SubjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.school.service.classschedule.AdjacentTimeSlotsUtils.adjacentTimeSlotIsPresent;
import static com.school.service.classschedule.AdjacentTimeSlotsUtils.lookForPreviousOrNextTimeSlotWithMatchingSubject;

@Service
public class TimeSlotAssigningService {
    private final TimeSlotQueryService timeSlotQueryService;
    private final SubjectService subjectService;
    private final ClassRoomService classRoomService;
    private final ClassScheduleConfig classScheduleConfig;
    private static final Logger log = LoggerFactory.getLogger(TimeSlotAssigningService.class);

    public TimeSlotAssigningService(TimeSlotQueryService timeSlotQueryService, SubjectService subjectService, ClassRoomService classRoomService, ClassScheduleConfig classScheduleConfig) {
        this.timeSlotQueryService = timeSlotQueryService;
        this.subjectService = subjectService;
        this.classRoomService = classRoomService;
        this.classScheduleConfig = classScheduleConfig;
    }

    @Transactional
    public TimeSlotDTO assignSubjectToTimeSlot(Long subjectId, Long timeSlotId) {
        try {
            List<ClassRoom> unassignedClassRooms = checkForAvailableClassRooms();
            TimeSlot currentTimeSlot = timeSlotQueryService.getTimeSlotById(timeSlotId);
            Subject currentSubject = subjectService.getSubjectById(subjectId);
            checkIfWeekHasMaxSubjectClasses(currentTimeSlot, currentSubject);

            log.debug("subjectId to be added: {} timeSlotId to be taken: {}", subjectId, timeSlotId);
            List<TimeSlot> currentScheduleEntryTimeSlots = timeSlotQueryService.getTimeSlotsByScheduleEntry(currentTimeSlot.getScheduleEntry());

            ClassRoom unassignedClassRoom;
            TimeSlot adjacentTimeSlot = lookForPreviousOrNextTimeSlotWithMatchingSubject(
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

        } catch (Exception e) {
            log.error("Error occurred while assigning subject to time slot: ", e);
            throw new IllegalStateException(e);
        }
    }

    private List<ClassRoom> checkForAvailableClassRooms() {
        List<ClassRoom> unassignedClassRoom = classRoomService.getUnassignedClassRooms();
        if (unassignedClassRoom.isEmpty()) {
            throw new IllegalStateException("No empty class rooms available, consider adding new class rooms!");
        } else {
            return unassignedClassRoom;
        }
    }

    private void checkIfWeekHasMaxSubjectClasses(TimeSlot timeSlot, Subject subject) {
        ClassSchedule schedule = timeSlot.getScheduleEntry().getClassSchedule();
        long scheduleSubjectClassCount = schedule.getScheduleEntries().stream()
                .flatMap(entry -> entry.getTimeSlots().stream()
                        .filter(ts -> ts.isNotBreak() &&
                                ts.getSubject() != null
                                && ts.getSubject().equals(subject)
                        )
                )
                .count();
        if (scheduleSubjectClassCount >= classScheduleConfig.getMaxSubjectClassPerWeek()) {
            throw new IllegalStateException("Max subject classes of " +
                    classScheduleConfig.getMaxSubjectClassPerWeek() +
                    " reached, cannot add more " + subject.getName() +
                    " subject to this schedule!"
            );
        }
    }

    private ClassRoom getMatchingSubjectClassRoom(TimeSlot currentTimeSlot) {
        return currentTimeSlot.getClassRoom();
    }

    private ClassRoom getRandomUnassignedClassRoom(List<ClassRoom> unassignedClassRooms) {
        log.debug("Timeslot has no previous or next slots taken, thus returning class room randomly");
        Collections.shuffle(unassignedClassRooms);
        return unassignedClassRooms.get(0);
    }

    private TimeSlot saveTimeSlotWithAssignedSubjectAndClassRoom(TimeSlot timeSlot, Subject subject, ClassRoom classRoom) {
        TimeSlot updatedTimeSlot = timeSlotQueryService.assignSubjectAndClassRoomToTimeSlot(timeSlot, subject, classRoom);
        classRoom.assignTimeSlot(updatedTimeSlot);
        classRoomService.updateWithAssignedTimeSlot(classRoom, updatedTimeSlot);
        log.info("Subject id: {} added to TimeSlot with id: {} and assigned ClassRoom no: {}",
                updatedTimeSlot.getSubject().getId(),
                updatedTimeSlot.getId(),
                updatedTimeSlot.getClassRoom().getRoomNumber()
        );
        return updatedTimeSlot;
    }
}