package carmen.provider.github;

import java.io.IOException;
import java.util.Map;

import carmen.set.github.User;
import carmen.set.github.RateLimit;

interface GithubProviderInterface {

    public RateLimit getCoreLimit() throws IOException;

    public RateLimit getSearchLimit() throws IOException;

    public User getUser(String name) throws IOException;
}