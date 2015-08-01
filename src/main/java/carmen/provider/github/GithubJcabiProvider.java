package carmen.provider.github;

import org.springframework.beans.factory.annotation.Value;

import com.jcabi.github.Coordinates;
import com.jcabi.github.RtGithub;
import com.jcabi.github.Github;
import com.jcabi.github.User.Smart;
import com.jcabi.github.Limits;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.IOException;

import java.lang.AssertionError;
import java.util.HashMap;
import java.util.Map;

import carmen.set.github.User;
import carmen.set.github.RateLimit;

public class GithubJcabiProvider implements GithubProviderInterface {

    public GithubJcabiProvider(RtGithub github) {
        this.github = github;
    }

    private RtGithub github;

    public Map getRateLimits() throws IOException {
        Limits privateLimits = github.limits();
        com.jcabi.github.Limit.Smart coreLimits = new com.jcabi.github.Limit.Smart(privateLimits.get("core"));
        com.jcabi.github.Limit.Smart searchLimits = new com.jcabi.github.Limit.Smart(privateLimits.get("search"));

        Map limits = new HashMap<String, RateLimit>();
        limits.put("core",  new RateLimit(coreLimits.limit(), coreLimits.remaining(), coreLimits.reset()));
        limits.put("search", new RateLimit(searchLimits.limit(), searchLimits.remaining(), searchLimits.reset()));

        return limits;
    }

    public User getUser(String name) throws IOException {
        com.jcabi.github.User privateUser = github.users().get(name);
        Smart user = new Smart(privateUser);

        try {
            return new User(
                new Long(user.id()),
                user.login(),
                user.hasName() ? user.name() : ""
            );
        } catch (AssertionError e) {
            return new User(null, name);
        }
    }
}