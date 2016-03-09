package com.cezarykluczynski.carmen.client.github;

import com.jcabi.github.RtGithub;
import com.jcabi.github.User.Smart;
import com.jcabi.github.Limits;

import java.io.IOException;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.json.JsonObject;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("githubJcabiClient")
public class GithubJcabiClient implements GithubClientInterface {

    @Autowired
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

            return User.builder()
                    .login(userJson.getString("login"))
                    .id((long) userJson.getInt("id"))
                    .name(getUserName(userJson))
                    .avatarUrl( userJson.getString("avatar_url"))
                    .type(userJson.getString("type"))
                    .siteAdmin(userJson.getBoolean("site_admin"))
                    .company( getUserCompany(userJson))
                    .blog(getUserBlog(userJson))
                    .location(getUserLocation(userJson))
                    .email(getUserEmail(userJson))
                    .hireable(getUserHireable(userJson))
                    .bio(getUserBio(userJson))
                    .build();
        } catch (AssertionError e) {
            return User.builder().login(name).build();
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
        return getFieldFromJsonObject(userJson, "name");
    }

    private String getUserCompany(JsonObject userJson) {
        return getFieldFromJsonObject(userJson, "company");
    }

    private String getUserLocation(JsonObject userJson) {
        return getFieldFromJsonObject(userJson, "location");
    }

    private String getUserBlog(JsonObject userJson) {
        return getFieldFromJsonObject(userJson, "blog");
    }

    private String getUserEmail(JsonObject userJson) {
        return getFieldFromJsonObject(userJson, "email");
    }

    private String getUserBio(JsonObject userJson) {
        return getFieldFromJsonObject(userJson, "bio");
    }

    private String getFieldFromJsonObject(JsonObject userJson, String field) {
        String value = null;

        try {
            value = userJson.getString(field);
        } catch (ClassCastException e) {
        } catch (IllegalStateException e) {
        }

        if (value == null) {
            value = "";
        }

        return value;
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
