package com.cezarykluczynski.carmen.common.util.pagination.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class Page<T> {

    T page;

    Pager pager;

}
