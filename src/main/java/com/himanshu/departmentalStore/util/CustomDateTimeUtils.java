package com.himanshu.departmentalStore.util;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for working with date and time.
 * Provides methods for formatting LocalDateTime objects to strings and parsing strings to LocalDateTime objects.
 */
@Component
public class CustomDateTimeUtils {
    /**
     * The default date-time format used by the utility methods.
     */
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * Formats a LocalDateTime object to a string using the default date-time format.
     * @param dateTime The LocalDateTime object to format.
     * @return A string representation of the LocalDateTime object.
     */
    public String formatLocalDateTime(final LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return dateTime.format(formatter);
    }

    /**
     * Parses a string representation of a date-time into a LocalDateTime object using the default date-time format.
     * @param dateTimeStr The string representation of the date-time to parse.
     * @return The LocalDateTime object parsed from the input string.
     */
    public LocalDateTime parseLocalDateTime(final String dateTimeStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        return LocalDateTime.parse(dateTimeStr, formatter);
    }
}
