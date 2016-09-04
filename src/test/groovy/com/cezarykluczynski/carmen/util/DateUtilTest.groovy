package com.cezarykluczynski.carmen.util

import spock.lang.Specification

import java.time.LocalDateTime

class DateUtilTest extends Specification {

    private static final int YEAR_DATE = 110
    private static final int YEAR_LOCAL_DATE_TIME = YEAR_DATE + 1900
    private static final int MONTH_DATE = 7
    private static final int MONTH_LOCAL_DATE_TIME = 8
    private static final int DAY = 4
    private static final int HOURS = 16
    private static final int MINUTES = 25
    private static final int SECONDS = 14

    private static final Date NOW = new Date()
    private static final Date DATE
    private static final LocalDateTime LOCAL_DATE_TIME

    static {
        TimeZone.setDefault DateUtil.getDefaultTimeZone()
        DATE = new Date(YEAR_DATE, MONTH_DATE, DAY, HOURS, MINUTES, SECONDS)
        LOCAL_DATE_TIME = LocalDateTime.of(YEAR_LOCAL_DATE_TIME, MONTH_LOCAL_DATE_TIME, DAY, HOURS, MINUTES, SECONDS)
    }

    def "returns current date"() {
        when:
        Date now = DateUtil.now()
        Date then = new Date()

        then:
        now.getTime() == NOW.getTime() || now.after(NOW)
        now.getTime() == then.getTime() || now.before(NOW)
    }

    def "converts null date to null"() {
        expect:
        DateUtil.toLocalDateTime(null) == null
    }

    def "converts date to local date time"() {
        when:
        LocalDateTime result = DateUtil.toLocalDateTime(DATE)

        then:
        result.year == YEAR_LOCAL_DATE_TIME
        result.month.value == MONTH_LOCAL_DATE_TIME
        result.dayOfMonth == DAY
        result.hour == HOURS
        result.minute == MINUTES
        result.second == SECONDS
        result.nano == 0
    }

    def "converts null local date time to null"() {
        expect:
        DateUtil.toDate(null) == null
    }

    def "converts local date time to date"() {
        when:
        Date result = DateUtil.toDate(LOCAL_DATE_TIME)

        then:
        result.year == YEAR_DATE
        result.month == MONTH_DATE
        result.date == DAY
        result.hours == HOURS
        result.minutes == MINUTES
        result.seconds == SECONDS
    }

    def "convert date to git readable format"() {
        expect:
        DateUtil.toGitReadableDateTime(DATE) == "1280939114"
    }

    def "default time zone if set to UTC"() {
        expect:
        DateUtil.getDefaultTimeZone().getID() == "UTC"
    }

}
