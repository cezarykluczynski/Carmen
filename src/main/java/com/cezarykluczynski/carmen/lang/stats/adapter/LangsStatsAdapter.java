package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat;
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat;

import java.util.List;
import java.util.Map;

public interface LangsStatsAdapter {

    List<Language> getSupportedLanguages();

    String getLinguistVersion();

    Map<Language, LineStat> describeRepository(String relativeDirectory, String commitHash);

    Map<Language, LineDiffStat> describeCommit(String relativeDirectory, String commitHash);

}
