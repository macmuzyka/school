package com.school.service.classschedule;

import com.school.controller.classschedule.TimeSlotPurgingController;
import com.school.model.dto.sclassschedule.TimeSlotDTO;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.classschedule.TimeSlotRepository;
import com.school.service.utils.EntityFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TimeSlotPurgingService {
    private final TimeSlotRepository timeSlotRepository;
    private final Logger log = LoggerFactory.getLogger(TimeSlotPurgingController.class);

    public TimeSlotPurgingService(TimeSlotRepository timeSlotRepository) {
        this.timeSlotRepository = timeSlotRepository;
    }

    public TimeSlotDTO purgeTimeSlot(Long timeSlotId) {
        try {
            TimeSlot timeSlot = EntityFetcher.getByIdOrThrow(timeSlotRepository::findById, timeSlotId, "TimeSlot");
            TimeSlot unlinked = unlinkSubjectAndClassRoomFromTimeSlot(timeSlot);
            log.debug("TimeSlot with id {} purged from relations", unlinked);
            return new TimeSlotDTO(unlinked);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private TimeSlot unlinkSubjectAndClassRoomFromTimeSlot(TimeSlot timeSlot) {
        timeSlot.setSubject(null);
        timeSlot.setClassRoom(null);
        return timeSlotRepository.save(timeSlot);
    }
}
