package carmen.provider.github;

import java.io.IOException;
import java.util.Map;
import java.util.List;

import carmen.set.github.User;
import carmen.set.github.RateLimit;
import carmen.util.PaginationAwareArrayList;

interface GithubProviderInterface {

    public RateLimit getCoreLimit() throws IOException;

    public RateLimit getSearchLimit() throws IOException;

    public User getUser(String name) throws IOException;

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException;

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException;
}