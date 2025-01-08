package com.school.service.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampUtils {
    public static final String toSecondFileFormat = "yyyy_MM_dd_HH_mm_ss";
    public static final String toDayFileFormat = "yyyy_MM_dd";
    public static final String toSecondDisplayFormat = "yyyy-MM-dd HH:mm:ss";

    public static String toSecondFileTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(toSecondFileFormat));
    }

    public static String toSecondDisplayTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(toSecondDisplayFormat));
    }

    public static String toDayFileTimestamp() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern(toDayFileFormat));
    }


}
