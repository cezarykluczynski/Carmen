package com.cezarykluczynski.carmen.common.util.pagination.calculator;

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager;
import com.google.common.base.Preconditions;

public class PagerCalculator {

    public static Integer getOffset(Pager pager) {
        Preconditions.checkNotNull(pager);
        return pager.getPageNumber() * pager.getItemsPerPage();
    }

    public static Integer toGitHubApiPageNumber(Pager pager) {
        Preconditions.checkNotNull(pager);
        return pager.getPageNumber() + 1;
    }

}
