package com.cezarykluczynski.carmen.util

import org.testng.Assert
import org.testng.annotations.Test

class DateUtilTest {

    static {
        TimeZone.setDefault TimeZone.getTimeZone("UTC")
    }


    @Test
    void toGitReadableDateTime() {
        Assert.assertEquals DateUtil.toGitReadableDateTime(new Date(110, 7, 4, 16, 25, 14)), "1280939114"
    }

}
