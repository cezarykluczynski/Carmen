package com.cezarykluczynski.carmen.provider.github;

import java.io.IOException;
import java.util.List;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

import org.kohsuke.github.GitHub;

public class GithubKohsukeProvider implements GithubProviderInterface {

    public GithubKohsukeProvider(GitHub github) {
        this.github = github;
    }

    private GitHub github;

    public RateLimit getCoreLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public RateLimit getSearchLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public User getUser(String name) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public List<Repository> getRepositories(String login) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

}
