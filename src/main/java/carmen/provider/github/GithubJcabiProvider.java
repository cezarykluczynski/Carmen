package carmen.provider.github;

import com.jcabi.github.RtGithub;
import com.jcabi.github.User.Smart;
import com.jcabi.github.Limits;

import java.io.IOException;

import java.lang.AssertionError;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import carmen.set.github.User;
import carmen.set.github.RateLimit;
import carmen.util.PaginationAwareArrayList;

public class GithubJcabiProvider implements GithubProviderInterface {

    public GithubJcabiProvider(RtGithub github) {
        this.github = github;
    }

    private RtGithub github;

    public RateLimit getCoreLimit() throws IOException {
        Limits privateLimits = github.limits();
        com.jcabi.github.Limit.Smart coreLimits = new com.jcabi.github.Limit.Smart(privateLimits.get("core"));
        return new RateLimit(
            "core",
            coreLimits.limit(),
            coreLimits.remaining(),
            correctSecondsLimit(coreLimits.reset())
        );
    }

    public RateLimit getSearchLimit() throws IOException {
        Limits privateLimits = github.limits();
        com.jcabi.github.Limit.Smart searchLimits = new com.jcabi.github.Limit.Smart(privateLimits.get("search"));
        return new RateLimit(
            "search",
            searchLimits.limit(),
            searchLimits.remaining(),
            correctSecondsLimit(searchLimits.reset())
        );
    }

    public User getUser(String name) throws IOException {
        com.jcabi.github.User privateUser = github.users().get(name);
        Smart user = new Smart(privateUser);

        try {
            return new User(
                new Long(user.id()),
                user.login(),
                user.hasName() ? user.name() : "",
                user.avatarUrl().toString(),
                user.type(),
                user.json().getBoolean("site_admin"), // There's some bug with boolean in Jcabi
                user.company(),
                user.blog().toString(),
                user.location(),
                user.email(),
                user.json().getBoolean("hireable") // There's some bug with boolean in Jcabi
            );
        } catch (AssertionError e) {
            return new User(null, name);
        }
    }

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    /**
     * Until Jcabi fix their implementation, this fix has to be here.
     * Basically, GitHub returns timestamp in seconds, and Date constructor expect miliseconds,
     * but no conversion is made.
     *
     * @see https://github.com/jcabi/jcabi-github/pull/1153
     */
    private Date correctSecondsLimit(Date limit) {
        return new Date(TimeUnit.MILLISECONDS.convert(
            limit.getTime(),
            TimeUnit.SECONDS
        ));
    }
}
