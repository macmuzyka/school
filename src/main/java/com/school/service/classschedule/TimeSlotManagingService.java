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

import java.util.Collections;
import java.util.List;

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
        log.debug("subjectId to be added: {} timeSlotId to be taken: {}", subjectId, timeSlotId);
        Subject subject = subjectService.getSubjectById(subjectId);
        TimeSlot timeSlot = timeSlotQueryService.getTimeSlotById(timeSlotId);
        List<ClassRoom> unassignedClassRooms = classRoomService.getUnassignedClassRooms();
        if (unassignedClassRooms.isEmpty()) {
            throw new IllegalStateException("No empty class rooms available, consider adding new class rooms!");
        } else {
            ClassRoom unassignedClassRoom;
            //TODO: find a way to ensure that if previous lesson time slot has the same subject,
            // lesson can take place in the same class room with the same number
            if (previousTimeSlotHasMatchingSubjectWithCurrent(timeSlot, subject)) {
                log.debug("Trying to find previous class room to reduce traffic within school");
                unassignedClassRoom = findPreviousTimeSlotClassRoom(timeSlot);
            } else {
                log.debug("Finding random class room");
                Collections.shuffle(unassignedClassRooms);
                unassignedClassRoom = unassignedClassRooms.get(0);
            }

            TimeSlot updatedTimeSlot = timeSlotQueryService.assignSubjectAndClassRoomToTimeSlot(timeSlot, subject, unassignedClassRoom);
            unassignedClassRoom.assignTimeSlot(updatedTimeSlot);
            classRoomService.updateWithAssignedTimeSlot(unassignedClassRoom, updatedTimeSlot);
            log.info("subject id: {} added to time slot id: {}", subjectId, timeSlotId);
            return new TimeSlotDTO(updatedTimeSlot);
        }
    }

    private boolean previousTimeSlotHasMatchingSubjectWithCurrent(TimeSlot timeSlot, Subject subject) {
        return false;
    }

    private ClassRoom findPreviousTimeSlotClassRoom(TimeSlot timeSlot) {
        return null;
    }
}