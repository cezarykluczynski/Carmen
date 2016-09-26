package com.cezarykluczynski.carmen.common.util.pagination.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Pager {

    Long pageNumber;

    Long pagesCount;

    Long itemsCount;

    Long itemsPerPage;

}
