package com.cezarykluczynski.carmen.util

import org.testng.annotations.Test
import org.testng.annotations.BeforeMethod
import org.testng.Assert

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.thenReturn
import static org.mockito.Mockito.verify

import com.cezarykluczynski.carmen.util.PaginationAwareArrayList

import java.util.Collection

import org.eclipse.egit.github.core.User as EgitUser
import org.eclipse.egit.github.core.client.PageIterator

class PaginationAwareArrayListTest {

    PaginationAwareArrayList paginationAwareArrayList

    @BeforeMethod
    void setUp() {
        paginationAwareArrayList = new PaginationAwareArrayList<EgitUser>()
    }

    @Test
    void extractPaginationDataFromIterator() {
        // setup
        PageIterator pageIteratorMock = mock PageIterator.class
        when pageIteratorMock.getNextPage() thenReturn 3
        when pageIteratorMock.hasNext() thenReturn true

        // exercise
        paginationAwareArrayList.extractPaginationDataFromIterator pageIteratorMock

        // assertion
        verify(pageIteratorMock).getNextPage()
        verify(pageIteratorMock).hasNext()
        Assert.assertEquals paginationAwareArrayList.getNextPage(), 3
        Assert.assertEquals paginationAwareArrayList.isLastPage(), false
    }

    @Test
    void extractPaginationDataFromCollection() {
        // setup
        Collection collectionMock = mock Collection.class
        when collectionMock.size() thenReturn 5

        // exercise
        paginationAwareArrayList.extractPaginationDataFromCollection collectionMock

        // assertion
        verify(collectionMock).size()
        Assert.assertEquals paginationAwareArrayList.getCount(), 5
    }

    @Test
    void addPaginationLimitAndOffset() {
        // exercise
        paginationAwareArrayList.addPaginationLimitAndOffset 10, 20

        // assertion
        Assert.assertEquals paginationAwareArrayList.getLimit(), 10
        Assert.assertEquals paginationAwareArrayList.getOffset(), 20
    }

}
