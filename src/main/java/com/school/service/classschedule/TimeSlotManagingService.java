package com.school.service.classschedule;

import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.SubjectRepository;
import com.school.repository.classschedule.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TimeSlotManagingService {
    private final TimeSlotRepository timeSlotRepository;
    private final SubjectRepository subjectRepository;
    private static final Logger log = LoggerFactory.getLogger(TimeSlotManagingService.class);

    public TimeSlotManagingService(TimeSlotRepository timeSlotRepository, SubjectRepository subjectRepository) {
        this.timeSlotRepository = timeSlotRepository;
        this.subjectRepository = subjectRepository;
    }

    public TimeSlot addSubjectToTimeSlot(Long subjectId, Long timeSlotId) {
        log.info("subjectId: {} timeSlotId: {}", subjectId, timeSlotId);
        //TODO: java.lang.IllegalStateException: getOutputStream() has already been called for this response
        // in frontend -> limit the returned response so no error occurs
        Optional<Subject> optionalSubject = subjectRepository.findById(subjectId);
        Optional<TimeSlot> optionalTimeSlot = timeSlotRepository.findById(timeSlotId);
        if (optionalSubject.isPresent() && optionalTimeSlot.isPresent()) {
            TimeSlot timeSlotToUpdate = optionalTimeSlot.get();
            timeSlotToUpdate.setSubject(optionalSubject.get());
            return timeSlotRepository.save(timeSlotToUpdate);
        } else {
            throw new IllegalArgumentException("Could not find subject id: " + subjectId +
                    " or time slot id: " + timeSlotId +
                    " subject is supposed to be added to!"
            );
        }
    }
}
