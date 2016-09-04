package com.cezarykluczynski.carmen.vcs.git.persistence.vendor.github;

import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.util.exec.executor.Executor;
import com.cezarykluczynski.carmen.util.exec.result.Result;
import com.cezarykluczynski.carmen.util.factory.DateFactory;
import com.cezarykluczynski.carmen.vcs.git.command.GitLogSinceCommand;
import com.cezarykluczynski.carmen.vcs.git.model.CommitHash;
import com.cezarykluczynski.carmen.vcs.git.util.DirectoryNameBuilder;
import com.cezarykluczynski.carmen.vcs.server.Server;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class GitHubRepositoryCommitsToPersistFinder {

    private DateFactory dateFactory;

    private Server server;

    @Autowired
    public GitHubRepositoryCommitsToPersistFinder(DateFactory dateFactory, Server server) {
        this.dateFactory = dateFactory;
        this.server = server;
    }

    public List<CommitHash> getCommitHashesToPersist(RepositoryClone repositoryClone) {
        Preconditions.checkNotNull(repositoryClone);
        List<CommitHash> commitHashes = Lists.newArrayList();
        Result result = getGitLogSinceCommandResult(repositoryClone);

        if (result.isSuccessFul()) {
            commitHashes.addAll(convertGitLogSinceResultToCommitHashes(result));
            return commitHashes;
        }

        // TODO Log it
        return null;
    }

    private Result getGitLogSinceCommandResult(RepositoryClone repositoryClone) {
        String cloneDirectory = DirectoryNameBuilder.buildCloneDirectory(server, repositoryClone);
        return Executor.execute(new GitLogSinceCommand(
                cloneDirectory,
                getSince(repositoryClone.getCommitsStatisticsUntil()),
                dateFactory.getEndOfAlreadyClosedMonth()));
    }

    private Date getSince(Date since) {
        return since != null ? since : dateFactory.getDateSinceForever();
    }

    private static List<CommitHash> convertGitLogSinceResultToCommitHashes(Result result) {
        return Arrays.stream(result.getOutput().split(System.getProperty("line.separator")))
                .filter(string -> !StringUtils.isWhitespace(string))
                .map(GitHubRepositoryCommitsToPersistFinder::convertLineToCommitHash)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static CommitHash convertLineToCommitHash(String line) {
        List<String> commitLogLineParts = Lists.newArrayList(line.split(","));

        if (commitLogLineParts.size() != 3) {
            return null;
        }

        return CommitHash.builder()
                .hash(commitLogLineParts.get(0))
                .date(new Date(Long.valueOf(commitLogLineParts.get(1))))
                .committerEmail(commitLogLineParts.get(2))
                .build();
    }

}
