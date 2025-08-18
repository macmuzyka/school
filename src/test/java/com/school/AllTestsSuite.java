package com.school;

import com.school.unit.ClassDispenserTests;
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
                StudentCounterAfterFileInputTest.class,
                StudentTests.class,
                SubjectTests.class,
        }
)

public class AllTestsSuite {
}
