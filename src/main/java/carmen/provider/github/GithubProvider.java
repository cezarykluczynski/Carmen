package carmen.provider.github;

import java.io.IOException;
import java.util.Map;

import carmen.set.github.User;
import carmen.set.github.RateLimit;

public class GithubProvider implements GithubProviderInterface {

    public GithubProvider(GithubProviderInterface provider) {
        this.provider = provider;
    }

    private GithubProviderInterface provider;

    public Map getRateLimits() throws IOException {
        return provider.getRateLimits();
    }

    public User getUser(String name) throws IOException {
        return provider.getUser(name);
    }
}