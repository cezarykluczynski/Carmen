package carmen.provider.github;

import java.io.IOException;

import carmen.set.github.User;

public class GithubProvider implements GithubProviderInterface {

    public GithubProvider(GithubProviderInterface provider) {
        this.provider = provider;
    }

    private GithubProviderInterface provider;

    public User getUser(String name) throws IOException {
        return provider.getUser(name);
    }
}