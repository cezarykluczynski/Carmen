package carmen.provider.github;

import java.io.IOException;

import carmen.set.github.User;

interface GithubProviderInterface {

    public User getUser(String name) throws IOException;
}