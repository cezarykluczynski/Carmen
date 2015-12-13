package com.cezarykluczynski.carmen.client.github;

import java.io.IOException;
import java.util.List;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

public interface GithubClientInterface {

    RateLimit getCoreLimit() throws IOException;

    RateLimit getSearchLimit() throws IOException;

    User getUser(String name) throws IOException;

    List<Repository> getRepositories(String login) throws IOException;

    PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException;

    PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException;

}
