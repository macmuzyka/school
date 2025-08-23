package com.school.unit;

import com.school.configuration.validate.TimeValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeValidatorTests {
    @Test
    public void shouldReturnLocalTimeEqualToInput() {
        LocalTime eightAm = TimeValidator.validateScheduleStart("8:00");
        assertEquals(8, eightAm.getHour());
        assertEquals(0, eightAm.getMinute());
        assertEquals(0, eightAm.getSecond());

        LocalTime nineAm20 = TimeValidator.validateScheduleStart("9:20");
        assertEquals(9, nineAm20.getHour());
        assertEquals(20, nineAm20.getMinute());
        assertEquals(0, nineAm20.getSecond());

        LocalTime tenAmF45 = TimeValidator.validateScheduleStart("10:45");
        assertEquals(10, tenAmF45.getHour());
        assertEquals(45, tenAmF45.getMinute());
        assertEquals(0, tenAmF45.getSecond());

        LocalTime twelveAm59 = TimeValidator.validateScheduleStart("12:59");
        assertEquals(12, twelveAm59.getHour());
        assertEquals(59, twelveAm59.getMinute());
        assertEquals(0, twelveAm59.getSecond());

        LocalTime firstPm1 = TimeValidator.validateScheduleStart("13:01");
        assertEquals(13, firstPm1.getHour());
        assertEquals(1, firstPm1.getMinute());
        assertEquals(0, firstPm1.getSecond());
    }

    @ParameterizedTest
    @ValueSource(strings = {"8:60", "25:01", "-1:32", "22:-32"})
    public void shouldReturnDefaultLocalTimeEqualToEightAMForBadInput(String input) {
        LocalTime defaultLocalTime = TimeValidator.validateScheduleStart(input);
        assertEquals(8, defaultLocalTime.getHour());
        assertEquals(0, defaultLocalTime.getMinute());
        assertEquals(0, defaultLocalTime.getSecond());
    }
}
