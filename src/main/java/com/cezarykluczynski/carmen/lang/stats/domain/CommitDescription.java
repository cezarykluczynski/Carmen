package com.cezarykluczynski.carmen.lang.stats.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CommitDescription {

    private String commitHash;

    private Map<Language, LineDiffStat> lineDiffStats;

}
