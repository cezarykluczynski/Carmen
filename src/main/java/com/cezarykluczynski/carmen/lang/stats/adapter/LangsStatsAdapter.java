package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineAction;

import java.util.List;
import java.util.Map;

public interface LangsStatsAdapter {

    List<Language> getSupportedLanguages();

    Map<Language, Integer> describeRepository(String relativeDirectory, String commitHash);

    Map<Language, Map<LineAction, Integer>> describeCommit(String relativeDirectory, String commitHash);

}
