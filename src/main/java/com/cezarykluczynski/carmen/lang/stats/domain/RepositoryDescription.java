package com.cezarykluczynski.carmen.lang.stats.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class RepositoryDescription {

    private String commitHash;

    private Map<Language, LineStat> lineStats;

}
