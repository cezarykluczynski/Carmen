package carmen.provider.github;

import com.jcabi.github.RtGithub;
import com.jcabi.github.User.Smart;
import com.jcabi.github.Limits;

import java.io.IOException;

import java.lang.AssertionError;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.lang.ClassCastException;
import java.lang.IllegalStateException;

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
                getUserName(user),
                user.avatarUrl().toString(),
                user.type(),
                user.json().getBoolean("site_admin"), // There's some bug with boolean in Jcabi
                getUserCompany(user),
                getUserBlog(user),
                getUserLocation(user),
                getUserEmail(user),
                getHireable(user)
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

    private String getUserName(com.jcabi.github.User.Smart user) {
        String name = null;

        try {
            if (user.hasName()) {
                name = user.name();
            }
        } catch (ClassCastException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }

        if (name == null) {
            name = "";
        }

        return name;
    }

    private String getUserCompany(com.jcabi.github.User.Smart user) {
        String company = null;

        try {
            company = user.company();
        } catch (ClassCastException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }

        if (company == null) {
            company = "";
        }

        return company;
    }

    private String getUserLocation(com.jcabi.github.User.Smart user) {
        String location = null;

        try {
            location = user.location();
        } catch (ClassCastException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }

        if (location == null) {
            location = "";
        }

        return location;
    }

    private String getUserBlog(com.jcabi.github.User.Smart user) {
        String blog = null;

        try {
            blog = user.blog().toString();
        } catch (ClassCastException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }

        if (blog == null) {
            blog = "";
        }

        return blog;
    }

    private String getUserEmail(com.jcabi.github.User.Smart user) {
        String email = null;

        try {
            email = user.email();
        } catch (ClassCastException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        }

        if (email == null) {
            email = "";
        }

        return email;
    }

    private Boolean getHireable(com.jcabi.github.User.Smart user) {
        Boolean hireable = false;

        try {
            hireable = user.json().getBoolean("hireable"); // There's some bug with boolean in Jcabi
        } catch (ClassCastException e) {
        } catch (IOException e) {
        } catch (IllegalStateException e) {
        } catch (NullPointerException e) {
        }

        return hireable;
    }

}
