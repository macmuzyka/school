package com.school.service.classschedule;

import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.ClassRoom;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.classschedule.TimeSlotRepository;
import com.school.service.utils.EntityFetcher;
import org.springframework.stereotype.Service;

@Service
public class TimeSlotQueryService {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotQueryService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlot getTimeSlotById(Long timeSlotId) {
        return EntityFetcher.getByIdOrThrow(timeSlotRepository::findById, timeSlotId, "TimeSlot");
    }

    public TimeSlot assignSubjectAndClassRoomToTimeSlot(TimeSlot timeSlotToUpdate, Subject subject, ClassRoom classRoom) {
        timeSlotToUpdate.setSubject(subject);
        timeSlotToUpdate.setClassRoom(classRoom);
        return timeSlotRepository.save(timeSlotToUpdate);
    }
}
