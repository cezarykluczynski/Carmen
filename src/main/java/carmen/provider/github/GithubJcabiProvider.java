package carmen.provider.github;

import org.springframework.beans.factory.annotation.Value;

import com.jcabi.github.Coordinates;
import com.jcabi.github.RtGithub;
import com.jcabi.github.Github;
import com.jcabi.github.User.Smart;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

import java.lang.AssertionError;

import carmen.set.github.User;

import java.lang.reflect.Method;

public class GithubJcabiProvider implements GithubProviderInterface {

    public GithubJcabiProvider(RtGithub rtGithub) {
        this.rtGithub = rtGithub;
    }

    private RtGithub rtGithub;

    public User getUser(String name) throws IOException, AssertionError {
        com.jcabi.github.User privateUser = rtGithub.users().get(name);
        Smart user = new Smart(privateUser);

        try {
            return new User(
                user.id(),
                user.hasName() ? user.name() : ""
            );
        } catch (AssertionError e) {
            return new User(0, "");
        }
    }
}