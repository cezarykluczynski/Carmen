package com.cezarykluczynski.carmen.util.factory

import com.cezarykluczynski.carmen.util.DateUtil
import spock.lang.Specification

class NowDateProviderTest extends Specification {

    private NowDateProvider nowDateProvider

    def setup() {
        nowDateProvider = new NowDateProvider()
    }

    def "creates now date"() {
        when:
        Date before = DateUtil.now()
        Date now = nowDateProvider.createNowDate()
        Date after = DateUtil.now()

        then:
        before.before(now) || before.equals(now)
        after.after(now) || after.equals(now)
    }

}
