package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription;
import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription;

import java.util.List;

public interface LangsStatsAdapter {

    List<Language> getSupportedLanguages();

    String getLinguistVersion();

    RepositoryDescription describeRepository(String relativeDirectory, String commitHash);

    CommitDescription describeCommit(String relativeDirectory, String commitHash);

}
