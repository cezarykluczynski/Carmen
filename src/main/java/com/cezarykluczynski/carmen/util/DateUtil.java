package com.cezarykluczynski.carmen.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil {

    static {
        TimeZone.setDefault(getDefaultTimeZone());
    }

    public static Date now() {
        return new Date();
    }

    public static LocalDateTime toLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(getDefaultZoneId()).toLocalDateTime();
    }

    public static Date toDate(LocalDateTime date) {
        if (date == null) {
            return null;
        }

        return Date.from(date.atZone(getDefaultZoneId()).toInstant());
    }

    public static String toGitReadableDateTime(Date date) {
        return String.valueOf(date.toInstant().atZone(getDefaultZoneId()).toEpochSecond());
    }

    public static TimeZone getDefaultTimeZone() {
        return TimeZone.getTimeZone("UTC");
    }

    private static ZoneId getDefaultZoneId() {
        return getDefaultTimeZone().toZoneId();
    }

}
