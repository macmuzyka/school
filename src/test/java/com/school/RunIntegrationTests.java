package com.school;

import com.school.integration.*;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(
        {
                SchoolApplicationTests.class,
                ClassTests.class,
                EmptyScheduleSchemaBuilderTests.class,
                ScheduleSeederTests.class,
                PreviousOrNextSlotsTakenTests.class,
                StudentCounterAfterFileInputTests.class,
                StudentTests.class,
                SubjectTests.class
        }
)
class RunIntegrationTests {
}
