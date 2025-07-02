package com.school.model.dto.sclassschedule;

import java.time.DayOfWeek;
import java.util.List;

public record ScheduleEntryDTO(DayOfWeek dayOfWeek, List<LessonUnitDTO> lessons) { }
