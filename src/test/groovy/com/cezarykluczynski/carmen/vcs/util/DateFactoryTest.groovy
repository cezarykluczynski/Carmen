package com.cezarykluczynski.carmen.vcs.util

import com.cezarykluczynski.carmen.util.factory.DateFactory
import com.cezarykluczynski.carmen.util.factory.NowDateProvider
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant

class DateFactoryTest extends Specification {

    @Shared
    private DateFactory dateFactory

    def setup() {
        NowDateProvider nowDateProvider = Mock NowDateProvider
        nowDateProvider.createNowDate() >>> [
                new Date(115, 0, 13, 12, 0, 0),
                new Date(115, 0, 15, 12, 0, 0),
                new Date(115, 1, 13, 12, 0, 0),
                new Date(115, 1, 15, 12, 0, 0),
                new Date(115, 2, 13, 12, 0, 0),
                new Date(115, 2, 15, 12, 0, 0),
                new Date(116, 0, 13, 12, 0, 0),
                new Date(116, 0, 15, 12, 0, 0),
                new Date(116, 1, 13, 12, 0, 0),
                new Date(116, 1, 15, 12, 0, 0),
                new Date(116, 2, 13, 12, 0, 0),
                new Date(116, 2, 15, 12, 0, 0),
                new Date(116, 3, 13, 12, 0, 0),
                new Date(116, 3, 15, 12, 0, 0)
        ]

        dateFactory = new DateFactory(nowDateProvider)
    }

    def "gets end of already closed month"() {
        expect:
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(114, 10, 30, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(114, 11, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(114, 11, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(115, 0, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(115, 0, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(115, 1, 28, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(115, 10, 30, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(115, 11, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(115, 11, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(116, 0, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(116, 0, 31, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(116, 1, 29, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(116, 1, 29, 23, 59, 59, 999)
        dateFactory.getEndOfAlreadyClosedMonth() == getDate(116, 2, 31, 23, 59, 59, 999)
    }

    def "gets beginning of not yet opened month"() {
        expect:
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(114, 11, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(115, 0, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(115, 0, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(115, 1, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(115, 1, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(115, 2, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(115, 11, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(116, 0, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(116, 0, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(116, 1, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(116, 1, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(116, 2, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(116, 2, 1, 0, 0, 0, 0)
        dateFactory.getBeginningOfNotYetOpenedMonth() == getDate(116, 3, 1, 0, 0, 0, 0)
    }

    def "gets date since forever"() {
        when:
        DateFactory dateFactory = new DateFactory(new NowDateProvider())
        Date date = Date.from(Instant.ofEpochMilli(-1))

        then:
        dateFactory.getDateSinceForever() == date
    }

    private static Date getDate(int year, int month, int date, int hrs, int min, int sec, int millis) {
        Date dateInstance = new Date(year, month, date, hrs, min, sec)
        dateInstance.setTime(dateInstance.getTime() + millis)
        return dateInstance
    }

}
