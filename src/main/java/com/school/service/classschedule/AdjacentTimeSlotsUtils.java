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

    public static boolean adjacentTimeSlotIsPresent(TimeSlot timeSlot) {
        return timeSlot != null;
    }

    public static TimeSlot lookForPreviousOrNextTimeSlotWithMatchingSubject(List<TimeSlot> scheduleEntrySlots, TimeSlot targetTimeSlot, Subject subject) {
        SlidingWindowIterator<TimeSlot> timeSlotsSlidingWindow =
                new SlidingWindowIterator<>(scheduleEntrySlots.stream()
                        .filter(TimeSlot::isNotBreak)
                        .sorted(Comparator.comparingLong(TimeSlot::getId))
                        .toList()
                );
        while (timeSlotsSlidingWindow.hasNext()) {
            log.debug("Current time slot: {}", timeSlotsSlidingWindow.windowMiddle());
            if (currentTimeSlotFoundInSlidingWindow(timeSlotsSlidingWindow.windowMiddle(), targetTimeSlot)) {
                TimeSlot matchingSlot = lookForAdjacentMatchingSubjectTimeSlot(timeSlotsSlidingWindow, subject);
                if (matchingSlot != null) {
                    return matchingSlot;
                }
            }
            timeSlotsSlidingWindow.next();
        }
        return null;
    }

    private static TimeSlot lookForAdjacentMatchingSubjectTimeSlot(SlidingWindowIterator<TimeSlot> timeSlotsSlidingWindow, Subject subject) {
        TimeSlot previousMatching = lookForMatchingPreviousTimeSlot(timeSlotsSlidingWindow, subject);
        if (previousMatching != null) {
            return previousMatching;
        }
        return lookForMatchingNextTimeSlot(timeSlotsSlidingWindow, subject);
    }

    private static boolean currentTimeSlotFoundInSlidingWindow(TimeSlot current, TimeSlot matchingTimeSlot) {
        return current.equals(matchingTimeSlot);
    }

    private static TimeSlot lookForMatchingPreviousTimeSlot(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots, Subject currentSubject) {
        if (previousSlotWithSubjectPresent(scheduleEntryTimeSlots)) {
            if (previousSubjectMatchesWithCurrent(scheduleEntryTimeSlots, currentSubject)) {
                log.debug("Found matching previous time slot: {}", scheduleEntryTimeSlots.peekPrevious());
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

    private static TimeSlot lookForMatchingNextTimeSlot(SlidingWindowIterator<TimeSlot> scheduleEntryTimeSlots, Subject currentSubject) {
        if (nextSlotWithSubjectPresent(scheduleEntryTimeSlots)) {
            if (nextSubjectMatchesWithCurrent(scheduleEntryTimeSlots, currentSubject)) {
                log.debug("Found matching next time slot: {}", scheduleEntryTimeSlots.peekNext());
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

