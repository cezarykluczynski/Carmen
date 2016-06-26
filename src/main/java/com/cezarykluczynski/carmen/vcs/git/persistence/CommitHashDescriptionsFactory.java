package com.cezarykluczynski.carmen.vcs.git.persistence;

import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter;
import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription;
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHashDescriptions;
import com.cezarykluczynski.carmen.vcs.git.util.DirectoryNameBuilder;
import com.cezarykluczynski.carmen.vcs.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommitHashDescriptionsFactory {

    private LangsStatsAdapter langsStatsAdapter;

    private Server server;

    @Autowired
    public CommitHashDescriptionsFactory(LangsStatsAdapter langsStatsAdapter, Server server) {
        this.langsStatsAdapter = langsStatsAdapter;
        this.server = server;
    }

    public CommitHashDescriptions createUsingRepositoryCloneAndCommitHash(RepositoryClone repositoryClone,
          CommitHash commitHash) {
        String cloneDirectory = DirectoryNameBuilder.buildCloneDirectory(server, repositoryClone);
        String hash = commitHash.getHash();

        CommitDescription commitDescription = langsStatsAdapter.describeCommit(cloneDirectory, hash);
        RepositoryDescription repositoryDescription = langsStatsAdapter.describeRepository(cloneDirectory, hash);

        return CommitHashDescriptions.builder()
                .commitDescription(commitDescription)
                .repositoryDescription(repositoryDescription)
                .commitHash(commitHash)
                .build();
    }

}
