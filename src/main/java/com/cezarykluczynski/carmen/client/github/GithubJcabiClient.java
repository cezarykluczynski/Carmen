package com.cezarykluczynski.carmen.client.github;

import com.jcabi.github.RtGithub;
import com.jcabi.github.User.Smart;
import com.jcabi.github.Limits;

import java.io.IOException;

import java.lang.AssertionError;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.json.JsonObject;

import java.lang.ClassCastException;
import java.lang.IllegalStateException;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;


public class GithubJcabiClient implements GithubClientInterface {

    public GithubJcabiClient(RtGithub github) {
        this.github = github;
    }

    private RtGithub github;

    public RateLimit getCoreLimit() throws IOException {
        Limits privateLimits = github.limits();
        com.jcabi.github.Limit.Smart coreLimits = new com.jcabi.github.Limit.Smart(privateLimits.get("core"));
        JsonObject coreLimitsJson = coreLimits.json();
        return new RateLimit(
            "core",
            coreLimitsJson.getInt("limit"),
            coreLimitsJson.getInt("remaining"),
            dateFromSecondsTimestamp(coreLimitsJson.getInt("reset"))
        );
    }

    public RateLimit getSearchLimit() throws IOException {
        Limits privateLimits = github.limits();
        com.jcabi.github.Limit.Smart searchLimits = new com.jcabi.github.Limit.Smart(privateLimits.get("search"));
        JsonObject searchLimitsJson = searchLimits.json();
        return new RateLimit(
            "search",
            searchLimitsJson.getInt("limit"),
            searchLimitsJson.getInt("remaining"),
            dateFromSecondsTimestamp(searchLimitsJson.getInt("reset"))
        );
    }

    public User getUser(String name) throws IOException {
        com.jcabi.github.User privateUser = github.users().get(name);
        Smart user = new Smart(privateUser);

        try {
            JsonObject userJson = user.json();

            return new User(
                new Long(userJson.getInt("id")),
                userJson.getString("login"),
                getUserName(userJson),
                userJson.getString("avatar_url"),
                userJson.getString("type"),
                userJson.getBoolean("site_admin"),
                getUserCompany(userJson),
                getUserBlog(userJson),
                getUserLocation(userJson),
                getUserEmail(userJson),
                getUserHireable(userJson)
            );
        } catch (AssertionError e) {
            return new User(null, name);
        }
    }

    public List<Repository> getRepositories(String login) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    private Date dateFromSecondsTimestamp(Integer limit) {
        return new Date(TimeUnit.MILLISECONDS.convert(limit, TimeUnit.SECONDS));
    }

    private String getUserName(JsonObject userJson) {
        String name = null;

        try {
            name = userJson.getString("name");
        } catch (ClassCastException e) {
        } catch (IllegalStateException e) {
        }

        if (name == null) {
            name = "";
        }

        return name;
    }

    private String getUserCompany(JsonObject userJson) {
        String company = null;

        try {
            company = userJson.getString("company");
        } catch (ClassCastException e) {
        } catch (IllegalStateException e) {
        }

        if (company == null) {
            company = "";
        }

        return company;
    }

    private String getUserLocation(JsonObject userJson) {
        String location = null;

        try {
            location = userJson.getString("location");
        } catch (ClassCastException e) {
        } catch (IllegalStateException e) {
        }

        if (location == null) {
            location = "";
        }

        return location;
    }

    private String getUserBlog(JsonObject userJson) {
        String blog = null;

        try {
            blog = userJson.getString("blog");
        } catch (ClassCastException e) {
        } catch (IllegalStateException e) {
        }

        if (blog == null) {
            blog = "";
        }

        return blog;
    }

    private String getUserEmail(JsonObject userJson) {
        String email = null;

        try {
           email = userJson.getString("email");
        } catch (ClassCastException e) {
        } catch (IllegalStateException e) {
        }

       if (email == null) {
           email = "";
       }

       return email;
    }

    private Boolean getUserHireable(JsonObject userJson) {
        Boolean hireable = false;

        try {
            hireable = userJson.getBoolean("hireable");
        } catch (ClassCastException e) {
        } catch (IllegalStateException e) {
        } catch (NullPointerException e) {
        }

        return hireable;
    }

}
