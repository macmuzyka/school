package com.school.model.dto.sclassschedule;

import java.time.DayOfWeek;

public record DaySubject(Long timeSlotId, DayOfWeek dayOfWeek, String subject, Integer roomNumber) { }
