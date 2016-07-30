package com.cezarykluczynski.carmen.util

import org.eclipse.egit.github.core.User as EgitUser
import org.eclipse.egit.github.core.client.PageIterator
import spock.lang.Specification

class PaginationAwareArrayListTest extends Specification {

    private PaginationAwareArrayList paginationAwareArrayList

    def setup() {
        paginationAwareArrayList = new PaginationAwareArrayList<EgitUser>()
    }

    def "extracts pagination data from iterator"() {
        given:
        PageIterator pageIteratorMock = Mock PageIterator

        when:
        paginationAwareArrayList.extractPaginationDataFromIterator pageIteratorMock

        then:
        1 * pageIteratorMock.getNextPage() >> 3
        1 * pageIteratorMock.hasNext() >> true
        paginationAwareArrayList.nextPage == 3
        !paginationAwareArrayList.lastPage
    }

    def "extracts pagination data from collection"() {
        given:
        Collection collectionMock = Mock Collection

        when:
        paginationAwareArrayList.extractPaginationDataFromCollection collectionMock

        then:
        1 * collectionMock.size() >> 5
        paginationAwareArrayList.getCount() == 5
    }

    def "adds pagination limit and offset"() {
        when:
        paginationAwareArrayList.addPaginationLimitAndOffset 10, 20

        then:
        paginationAwareArrayList.getLimit() == 10
        paginationAwareArrayList.getOffset() == 20
    }

}
