package com.cezarykluczynski.carmen.common.util.pagination.factory;

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Iterators;
import org.eclipse.egit.github.core.client.PageIterator;

public class PagerFactory {

    public static Pager ofOffsetAndLimit(Integer offset, Integer limit) {
        Preconditions.checkNotNull(offset);
        Preconditions.checkNotNull(limit);
        checkOffsetAndLimit(offset, limit);

        return Pager.builder()
                .itemsPerPage(limit)
                .pageNumber(offset / limit)
                .build();
    }

    public static Pager ofPageAndLimit(Integer page, Integer limit) {
        Preconditions.checkNotNull(page);
        Preconditions.checkNotNull(limit);
        Preconditions.checkState(limit > 0, "limit should be greater than 0");
        Preconditions.checkState(page > -1, "page should not be lesser than 0");

        return Pager.builder()
                .pageNumber(page)
                .itemsPerPage(limit)
                .build();
    }

    public static Pager of(PageIterator pageIterator, Pager requestPager) {
        Integer nextPage = pageIterator.getNextPage() < 0 ? null : pageIterator.getNextPage() - 1;
        Integer pagesCount = pageIterator.getLastPage() < 0 ? 1 : pageIterator.getLastPage();

        return Pager.builder()
                .itemsCount(Iterators.size(pageIterator.iterator()))
                .pagesCount(pagesCount)
                .itemsPerPage(requestPager.getItemsPerPage())
                .pageNumber(nextPage)
                .build();
    }

    private static void checkOffsetAndLimit(Integer offset, Integer limit) {
        if (offset < 0L || offset % limit != 0) {
            throw new RuntimeException();
        }
    }

}
