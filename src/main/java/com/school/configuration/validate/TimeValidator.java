package com.school.configuration.validate;

import java.time.LocalTime;

public class TimeValidator {
    public static LocalTime validateScheduleStart(String scheduleStart) {
        try {
            String[] hourAndMinutes = scheduleStart.split(":");
            int hour = Integer.parseInt(hourAndMinutes[0]);
            if (hour < 0 || hour > 24) {
                throw new Exception("Hour from config is out of range");
            }
            int minute = getMinute(hourAndMinutes);
            return LocalTime.of(hour, minute);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return LocalTime.of(8, 0);
        }
    }

    private static int getMinute(String[] hourAndMinutes) throws Exception {
        int minute;
        String decimalValue = String.valueOf(hourAndMinutes[1].charAt(0));
        if (decimalValue.equals("0")) {
            minute = Integer.parseInt(String.valueOf(hourAndMinutes[1].charAt(1)));
        } else if (Integer.parseInt(decimalValue) >= 1) {
            minute = Integer.parseInt(hourAndMinutes[1]);
        } else if (Integer.parseInt(hourAndMinutes[1]) > 59) {
            minute = 59;
        } else {
            throw new Exception("Minute from config is out of range");
        }
        return minute;
    }
}
