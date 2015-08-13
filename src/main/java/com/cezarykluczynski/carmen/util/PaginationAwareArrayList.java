package com.cezarykluczynski.carmen.util;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.PageIterator;

public class PaginationAwareArrayList<E> extends ArrayList<E> {

    private Integer offset;

    private Integer limit;

    private Integer count;

    private Integer nextPage;

    private Boolean lastPage;

    public void addPaginationData(PageIterator pageIterator, Collection<User> collection) {
        nextPage = pageIterator.getNextPage();
        lastPage = !pageIterator.hasNext();
        count = collection.size();
    }

    public void addPaginationData(Integer offset, Integer limit) {
        this.offset = offset;
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public Integer getCount() {
        return count;
    }

    public Integer getNextPage() {
        return nextPage;
    }

    public Boolean isLastPage() {
        return lastPage;
    }

}