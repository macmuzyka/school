package com.school.model.dto.sclassschedule;

import java.util.List;
import java.util.Map;

record ClassScheduleDTO(Map<String, List<ClassScheduleDisplayDTO>> schedule) { }
