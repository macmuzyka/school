package com.school.service.classschedule;

import com.school.model.entity.Subject;
import com.school.model.entity.classschedule.TimeSlot;
import com.school.service.utils.SlidingWindowIterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;

public class AdjacentTimeSlotsUtils {
    private static final Logger log = LoggerFactory.getLogger(AdjacentTimeSlotsUtils.class);

    public static TimeSlot findPreviousOrNextTimeSlotWithMatchingSubject(List<TimeSlot> scheduleEntrySlots, TimeSlot targetTimeSlot, Subject subject) {
        SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots =
                new SlidingWindowIterator<>(scheduleEntrySlots.stream()
                        .filter(TimeSlot::isNotBreak)
                        .sorted(Comparator.comparingLong(TimeSlot::getId))
                        .toList()
                );
        while (scheduleEntryTimeSlots.hasNext()) {
            log.debug("Current time slot: {}", scheduleEntryTimeSlots.current());
            if (currentTimeSlotFoundInScheduleEntry(scheduleEntryTimeSlots.current(), targetTimeSlot)) {
                TimeSlot previousMatching = findMatchingPreviousTimeSlot(scheduleEntryTimeSlots, subject);
                if (previousMatching != null) {
                    return previousMatching;
                }
                TimeSlot nextMatching = findMatchingNextTimeSlot(scheduleEntryTimeSlots, subject);
                if (nextMatching != null) {
                    return nextMatching;
                }
            }
            scheduleEntryTimeSlots.next();
        }
        return null;
    }

    public static boolean adjacentTimeSlotIsPresent(TimeSlot timeSlot) {
        return timeSlot != null;
    }

    private static boolean currentTimeSlotFoundInScheduleEntry(TimeSlot current, TimeSlot matchingTimeSlot) {
        return current.equals(matchingTimeSlot);
    }

    private static TimeSlot findMatchingPreviousTimeSlot(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots, Subject currentSubject) {
        if (previousSlotWithSubjectPresent(scheduleEntryTimeSlots)) {
            if (previousSubjectMatchesWithCurrent(scheduleEntryTimeSlots, currentSubject)) {
                log.info("Found matching previous time slot: {}", scheduleEntryTimeSlots.peekPrevious());
                return scheduleEntryTimeSlots.peekPrevious();
            }
        }
        return null;
    }

    private static boolean previousSlotWithSubjectPresent(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots) {
        return scheduleEntryTimeSlots.peekPrevious() != null && scheduleEntryTimeSlots.peekPrevious().getSubject() != null;
    }

    private static boolean previousSubjectMatchesWithCurrent(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots, Subject currentSubject) {
        return scheduleEntryTimeSlots.peekPrevious().getSubject().equals(currentSubject);
    }

    private static TimeSlot findMatchingNextTimeSlot(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots, Subject currentSubject) {
        if (nextSlotWithSubjectPresent(scheduleEntryTimeSlots)) {
            if (nextSubjectMatchesWithCurrent(scheduleEntryTimeSlots, currentSubject)) {
                log.info("Found matching next time slot: {}", scheduleEntryTimeSlots.peekNext());
                return scheduleEntryTimeSlots.peekNext();
            }
        }
        return null;
    }

    private static boolean nextSlotWithSubjectPresent(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots) {
        return scheduleEntryTimeSlots.peekNext() != null && scheduleEntryTimeSlots.peekNext().getSubject() != null;
    }

    private static boolean nextSubjectMatchesWithCurrent(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots, Subject currentSubject) {
        return scheduleEntryTimeSlots.peekNext().getSubject().equals(currentSubject);
    }
}

