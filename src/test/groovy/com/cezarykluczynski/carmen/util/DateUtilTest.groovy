package com.cezarykluczynski.carmen.util

import spock.lang.Specification

class DateUtilTest extends Specification {

    static {
        TimeZone.setDefault DateUtil.getDefaultTimeZone()
    }

    void toGitReadableDateTime() {
        expect:
        DateUtil.toGitReadableDateTime(new Date(110, 7, 4, 16, 25, 14)) == "1280939114"
    }

}
