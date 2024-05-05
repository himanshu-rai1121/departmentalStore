package com.himanshu.departmentalStore.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CustomDateTimeUtilsTest {

    @Autowired
    private CustomDateTimeUtils dateTimeUtils;

    @Test
    public void testFormatLocalDateTime() {
        LocalDateTime dateTime = LocalDateTime.of(2022, 5, 3, 10, 15, 30);
        String expectedFormattedDateTime = "2022-05-03T10:15:30";
        String formattedDateTime = dateTimeUtils.formatLocalDateTime(dateTime);
        assertEquals(expectedFormattedDateTime, formattedDateTime);
    }

    @Test
    public void testParseLocalDateTime() {
        String dateTimeStr = "2022-05-03T10:15:30";
        LocalDateTime expectedDateTime = LocalDateTime.of(2022, 5, 3, 10, 15, 30);
        LocalDateTime parsedDateTime = dateTimeUtils.parseLocalDateTime(dateTimeStr);
        assertEquals(expectedDateTime, parsedDateTime);
    }
}
