package com.cezarykluczynski.carmen.common.util.pagination.calculator

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager
import spock.lang.Specification
import spock.lang.Unroll

class PagerCalculatorTest extends Specification {

    @Unroll
    def "#pager should have calculated offset of #offset"() {
        expect:
        PagerCalculator.getOffset(pager) == offset

        where:
        pager                                       | offset
        new Pager(pageNumber: 0, itemsPerPage: 10)  | 0L
        new Pager(pageNumber: 5, itemsPerPage: 5)   | 25L
        new Pager(pageNumber: 7, itemsPerPage: 3)   | 21L
        new Pager(pageNumber: 10, itemsPerPage: 10) | 100L
    }

}
