package com.cezarykluczynski.carmen.util;

import java.util.ArrayList;
import java.util.Collection;

import lombok.Data;
import org.eclipse.egit.github.core.client.PageIterator;

@Data
public class PaginationAwareArrayList<E> extends ArrayList<E> {

    private Integer offset;

    private Integer limit;

    private Integer count;

    private Integer nextPage;

    private boolean lastPage;

    public void extractPaginationDataFromIterator(PageIterator pageIterator) {
        setNextPage(pageIterator.getNextPage());
        setLastPage(!pageIterator.hasNext());
    }

    public void extractPaginationDataFromCollection(Collection collection) {
        setCount(collection.size());
    }

    public void addPaginationLimitAndOffset(Integer paginationLimit, Integer paginationOffset) {
        setLimit(paginationLimit);
        setOffset(paginationOffset);
    }

}
