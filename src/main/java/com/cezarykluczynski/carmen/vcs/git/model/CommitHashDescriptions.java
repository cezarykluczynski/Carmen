package com.cezarykluczynski.carmen.vcs.git.model;

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription;
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CommitHashDescriptions {

    private CommitHash commitHash;

    private CommitDescription commitDescription;

    private RepositoryDescription repositoryDescription;

}
