package com.cezarykluczynski.carmen.common.util.pagination.dto;

import lombok.*;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class Pager {

    Integer pageNumber;

    Integer pagesCount;

    Integer itemsCount;

    Integer itemsPerPage;

}
