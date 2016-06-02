package com.cezarykluczynski.carmen.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class DateUtil {

    public static Date now() {
        return new Date();
    }

    public static LocalDateTime convert(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String toGitReadableDateTime(Date date) {
        return Integer.toString(Math.round(date.getTime() / 1000));
    }

}
