package com.cezarykluczynski.carmen.util;

public enum DateTimeConstants {

    MILLISECONDS_IN_SECOND(1000),
    MILLISECONDS_IN_MINUTE(60000),
    MILLISECONDS_IN_HOUR(3600000),
    MILLISECONDS_IN_DAY(86400000),
    MILLISECONDS_IN_WEEK(604800000),

    SECONDS_IN_MINUTE(60),
    SECONDS_IN_HOUR(3600),
    SECONDS_IN_DAY(86400),
    SECONDS_IN_WEEK(604800),


    MINUTES_IN_HOUR(60),
    MINUTES_IN_DAY(1440),
    MINUTES_IN_WEEK(10080),

    HOURS_IN_DAY(24),
    HOURS_IN_WEEK(168);

    private final int value;

    DateTimeConstants(final int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
