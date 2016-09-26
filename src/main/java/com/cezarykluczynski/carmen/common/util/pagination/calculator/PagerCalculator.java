package com.cezarykluczynski.carmen.common.util.pagination.calculator;

import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager;

public class PagerCalculator {

    public static Long getOffset(Pager pager) {
        return pager.getPageNumber() * pager.getItemsPerPage();
    }

}
