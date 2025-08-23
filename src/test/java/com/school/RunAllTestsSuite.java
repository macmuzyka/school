package com.school;

import com.school.integration.*;
import com.school.unit.ClassDispenserTests;
import com.school.unit.TimeSlotUtilsTests;
import com.school.unit.TimeValidatorTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(
        {
                SchoolApplicationTests.class,
                ClassTests.class,
                ClassDispenserTests.class,
                EmptyScheduleSchemaBuilderTests.class,
                ScheduleSeederTests.class,
                PreviousOrNextSlotsTakenTests.class,
                StudentCounterAfterFileInputTests.class,
                StudentTests.class,
                TimeSlotUtilsTests.class,
                TimeValidatorTests.class,
                SubjectTests.class
        }
)
class RunAllTestsSuite {
}
