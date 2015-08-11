package carmen.provider.github;

import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

import carmen.set.github.User;
import carmen.set.github.RateLimit;

interface GithubProviderInterface {

    public RateLimit getCoreLimit() throws IOException;

    public RateLimit getSearchLimit() throws IOException;

    public User getUser(String name) throws IOException;

    public ArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException;

    public ArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException;
}