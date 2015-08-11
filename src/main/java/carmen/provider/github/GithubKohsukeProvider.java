package carmen.provider.github;

import java.io.IOException;
import java.util.ArrayList;

import carmen.set.github.User;
import carmen.set.github.RateLimit;

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

    public ArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public ArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

}
