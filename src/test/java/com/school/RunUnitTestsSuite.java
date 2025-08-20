package com.school;

import com.school.unit.ClassDispenserTests;
import com.school.unit.TimeSlotUtilsTests;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses(
        {
                TimeSlotUtilsTests.class,
                ClassDispenserTests.class
        }
)
class RunUnitTestsSuite {
}
