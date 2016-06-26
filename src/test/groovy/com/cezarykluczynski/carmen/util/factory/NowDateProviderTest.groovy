package com.cezarykluczynski.carmen.util.factory

import com.cezarykluczynski.carmen.util.DateUtil
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class NowDateProviderTest {

    private NowDateProvider nowDateProvider

    @BeforeMethod
    void setup() {
        nowDateProvider = new NowDateProvider()
    }

    @Test
    void createNowDate() {
        Date before = DateUtil.now()
        Date now = nowDateProvider.createNowDate()
        Date after = DateUtil.now()

        Assert.assertTrue(before.before(now) || before.equals(now))
        Assert.assertTrue(after.after(now) || after.equals(now))
    }

}
