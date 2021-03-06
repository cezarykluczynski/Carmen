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

    def "null pager produces exception when retrieving offset"() {
        when:
        PagerCalculator.getOffset(null)

        then:
        thrown(NullPointerException)
    }

    @Unroll
    def "#gitHubPageNumber is extracted from #pager"() {
        expect:
        PagerCalculator.toGitHubApiPageNumber(pager) == gitHubPageNumber

        where:
        pager                     | gitHubPageNumber
        new Pager(pageNumber: 0L) | 1
        new Pager(pageNumber: 1L) | 2
        new Pager(pageNumber: 2L) | 3
        new Pager(pageNumber: 3L) | 4
    }

    def "null pager produces exception when converting to GitHub page number"() {
        when:
        PagerCalculator.toGitHubApiPageNumber(null)

        then:
        thrown(NullPointerException)
    }

}
