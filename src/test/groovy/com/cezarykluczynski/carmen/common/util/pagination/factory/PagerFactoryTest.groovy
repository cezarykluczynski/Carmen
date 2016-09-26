package com.cezarykluczynski.carmen.common.util.pagination.factory

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager
import spock.lang.Specification
import spock.lang.Unroll

class PagerFactoryTest extends Specification {

    @Unroll
    def "#offset and #limit should procude #pager"() {
        expect:
        PagerFactory.ofOffsetAndLimit(offset, limit) == pager

        where:
        offset     | limit  | pager
        0          | 10     | new Pager(pageNumber: 0, itemsPerPage: 10)
        20         | 20     | new Pager(pageNumber: 1, itemsPerPage: 20)
        50         | 10     | new Pager(pageNumber: 5, itemsPerPage: 10)
        100        | 2      | new Pager(pageNumber: 50, itemsPerPage: 2)
    }

    @Unroll
    def "#offset and #limit should procude NullPointerException"() {
        when:
        PagerFactory.ofOffsetAndLimit(offset, limit)

        then:
        thrown(NullPointerException)

        where:
        offset | limit
        null   | 10
        0      | null
        null   | null
    }

    @Unroll
    def "#offset and #limit should procude exception"() {
        when:
        PagerFactory.ofOffsetAndLimit(offset, limit)

        then:
        thrown(RuntimeException)

        where:
        offset | limit
        5      | 10
        15     | 20
        49     | 13
        99     | 4
    }

}
