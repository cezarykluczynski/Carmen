package com.cezarykluczynski.carmen.common.util.pagination.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(staticName = "of")
public class Slice<T> {

    List<T> page;

    Pager pager;

}
