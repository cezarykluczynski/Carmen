package com.cezarykluczynski.carmen.common.util.pagination.factory

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager
import org.eclipse.egit.github.core.client.PageIterator
import spock.lang.Specification
import spock.lang.Unroll

class PagerFactoryTest extends Specification {

    @Unroll
    def "#offset and #limit should produce #pager"() {
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
    def "#offset and #limit should produce NullPointerException"() {
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
    def "#offset and #limit should produce RuntimeException"() {
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

    @Unroll
    def "#page and #limit should produce #pager"() {
        expect:
        PagerFactory.ofPageAndLimit(page, limit) == pager

        where:
        page | limit | pager
        0    | 10    | new Pager(pageNumber: 0, itemsPerPage: 10)
        2    | 20    | new Pager(pageNumber: 2, itemsPerPage: 20)
        3    | 30    | new Pager(pageNumber: 3, itemsPerPage: 30)
    }

    @Unroll
    def "#page and #limit should procude NullPointerException"() {
        when:
        PagerFactory.ofPageAndLimit(page, limit)

        then:
        thrown(NullPointerException)

        where:
        page | limit
        null | 10
        0    | null
        null | null
    }


    @Unroll
    def "#page and #limit should produce RuntimeException"() {
        when:
        PagerFactory.ofPageAndLimit(page, limit)

        then:
        thrown(RuntimeException)

        where:
        page  | limit
        -1    | 10
        1     | 0
    }

    def "#pageIterator and #requestPager should produce #pager"() {
        expect:
        PagerFactory.of(pageIterator, requestPager) == pager

        where:
        pageIterator                   | requestPager                | pager
        createPageIterator(30, 2, 5)   | new Pager(itemsPerPage: 2)  | new Pager(pageNumber: 1, pagesCount: 5, itemsCount: 30, itemsPerPage: 2)
        createPageIterator(10, 1, 2)   | new Pager(itemsPerPage: 5)  | new Pager(pageNumber: 0, pagesCount: 2, itemsCount: 10, itemsPerPage: 5)
        createPageIterator(120, 7, 15) | new Pager(itemsPerPage: 20) | new Pager(pageNumber: 6, pagesCount: 15, itemsCount: 120, itemsPerPage: 20)
        createPageIterator(120, 7, 15) | new Pager(itemsPerPage: 20) | new Pager(pageNumber: 6, pagesCount: 15, itemsCount: 120, itemsPerPage: 20)
        createPageIterator(10, -1, 15) | new Pager(itemsPerPage: 20) | new Pager(pageNumber: null, pagesCount: 15, itemsCount: 10, itemsPerPage: 20)
        createPageIterator(10, 1, -1)  | new Pager(itemsPerPage: 20) | new Pager(pageNumber: 0, pagesCount: 1, itemsCount: 10, itemsPerPage: 20)
    }

    private PageIterator<?> createPageIterator(Integer size, Integer nextPage, Integer lastPage) {
        Integer count = 0
        return Mock(PageIterator) {
            getNextPage() >> nextPage
            getLastPage() >> lastPage
            iterator() >> Mock(Iterator) {
                hasNext() >> {
                    count++
                    return count <= size
                }
            }
        }
    }

}
