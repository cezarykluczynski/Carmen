package carmen.provider.github;

import java.io.IOException;
import java.util.Map;

import carmen.set.github.User;
import carmen.set.github.RateLimit;

interface GithubProviderInterface {

    public Map getRateLimits() throws IOException;

    public User getUser(String name) throws IOException;
}