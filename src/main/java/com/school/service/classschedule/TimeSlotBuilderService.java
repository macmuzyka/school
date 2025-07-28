package com.school.service.classschedule;

import com.school.configuration.ApplicationConfig;
import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.ClassRoom;
import com.school.model.entity.classschedule.ScheduleEntry;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.repository.classschedule.TimeSlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotBuilderService {
    private final TimeSlotRepository timeSlotRepository;
    private final ApplicationConfig applicationConfig;
    private static final Logger log = LoggerFactory.getLogger(TimeSlotBuilderService.class);

    public TimeSlotBuilderService(TimeSlotRepository timeSlotRepository, ApplicationConfig applicationConfig) {
        this.timeSlotRepository = timeSlotRepository;
        this.applicationConfig = applicationConfig;
    }

    public List<TimeSlot> buildTimeSlots(ScheduleEntry scheduleEntry) {
        List<TimeSlot> timeSlots = new ArrayList<>();
        TimeSlot firstTimeSlot = timeSlotRepository.save(buildFirstTimeSlot(scheduleEntry));
        timeSlots.add(firstTimeSlot);

        buildRemainingTimeSlots(firstTimeSlot.getEndTime(), scheduleEntry, timeSlots);
        log.debug("Time slots for schedule entry id {} for a class {} for a day {}:",
                scheduleEntry.getId(),
                scheduleEntry.getClassSchedule().getSchoolClass().getName(),
                scheduleEntry.getDayOfWeek()
        );
        timeSlots.forEach(it -> log.debug(it.toString()));
        return timeSlots;
    }

    //TODO: special case object pattern to prevent passing subject as null in empty time slot
    private TimeSlot buildFirstTimeSlot(ScheduleEntry scheduleEntry) {
        return new TimeSlot(
                applicationConfig.getLessonScheduleStart(),
                applicationConfig.getLessonScheduleStart().plusMinutes(applicationConfig.getLessonDuration()),
                null,
                scheduleEntry,
                false
        );
    }

    private void buildRemainingTimeSlots(LocalTime previousSlotEnding, ScheduleEntry scheduleEntry, List<TimeSlot> timeSlots) {
        boolean slotIsBreak;
        LocalTime slotBeginning;
        LocalTime slotEnding = previousSlotEnding;
        int maxTimeSlots = calculateMaxSlots();

        for (int currentSlot = 2; currentSlot <= maxTimeSlots; currentSlot++) {
            slotBeginning = slotEnding;
            if (slotIsBreak(currentSlot)) {
                slotIsBreak = true;
                if (slotIsLongBreak(currentSlot)) {
                    slotEnding = addLongBreak(slotEnding);
                } else {
                    slotEnding = addShortBreak(slotEnding);
                }
            } else {
                slotIsBreak = false;
                slotEnding = addLessonDuration(slotEnding);
            }
            TimeSlot toSave = timeSlotRepository.save(
                    new TimeSlot(
                            slotBeginning,
                            slotEnding,
                            null,
                            scheduleEntry,
                            slotIsBreak
                    )
            );
            timeSlots.add(toSave);
        }
    }

    private int calculateMaxSlots() {
        return (applicationConfig.getMaxLessons() * 2) - 1;
    }

    private boolean slotIsBreak(int timeSlot) {
        return timeSlot % 2 == 0;
    }

    private LocalTime addLongBreak(LocalTime startingPoint) {
        return startingPoint.plusMinutes(applicationConfig.getLongBreakDuration());
    }

    private LocalTime addShortBreak(LocalTime startingPoint) {
        return startingPoint.plusMinutes(applicationConfig.getShortBreakDuration());
    }

    private LocalTime addLessonDuration(LocalTime startingPoint) {
        return startingPoint.plusMinutes(applicationConfig.getLessonDuration());
    }

    private boolean slotIsLongBreak(int timeSlot) {
        int firstLongBreak = applicationConfig.getFirstLongBreak() * 2;
        int secondLongBreak = applicationConfig.getSecondLongBreak() * 2;
        return timeSlot == firstLongBreak || timeSlot == secondLongBreak;
    }
}
