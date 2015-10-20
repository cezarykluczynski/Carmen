package com.cezarykluczynski.carmen.provider.github;

import java.io.IOException;
import java.util.List;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

public interface GithubProviderInterface {

    public RateLimit getCoreLimit() throws IOException;

    public RateLimit getSearchLimit() throws IOException;

    public User getUser(String name) throws IOException;

    public List<Repository> getRepositories(String login) throws IOException;

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException;

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException;

}
