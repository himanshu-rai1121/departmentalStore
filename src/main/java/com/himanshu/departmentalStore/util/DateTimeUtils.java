package com.himanshu.departmentalStore.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    // Method to format LocalDateTime to a string
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }

    // Method to parse a string to LocalDateTime
    public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
