package com.school;

import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ClassTest.class, StudentCounterAfterFileInputTest.class})
@IncludeClassNamePatterns(".*Test")
public class AllTestsSuite { }
