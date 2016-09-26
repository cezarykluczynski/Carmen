package com.cezarykluczynski.carmen.common.util.pagination.factory;

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager;
import com.google.common.base.Preconditions;

public class PagerFactory {

    public static Pager ofOffsetAndLimit(Long offset, Long limit) {
        Preconditions.checkNotNull(offset);
        Preconditions.checkNotNull(limit);

        if (offset < 0L || offset % limit != 0) {
            throw new RuntimeException();
        }

        return Pager.builder()
                .itemsPerPage(limit)
                .pageNumber(offset / limit)
                .build();
    }

}
