package com.cezarykluczynski.carmen.client.github;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.dao.github.RateLimitDAO;
import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

import java.util.Date;
import java.util.List;

@Component
public class GithubClient implements GithubClientInterface {

    public GithubClient(
        GithubClientInterface githubJcabiClient,
        GithubClientInterface githubKohsukeClient,
        GithubClientInterface githubEgitClient,
        RateLimitDAO rateLimitDAOImpl
    ) {
        this.githubJcabiClient = githubJcabiClient;
        this.githubKohsukeClient = githubKohsukeClient;
        this.githubEgitClient = githubEgitClient;
        this.rateLimitDAOImpl = rateLimitDAOImpl;
    }

    private GithubClientInterface githubJcabiClient;

    private GithubClientInterface githubKohsukeClient;

    private GithubClientInterface githubEgitClient;

    private RateLimitDAO rateLimitDAOImpl;

    public RateLimit getCoreLimit() throws IOException {
        return githubJcabiClient.getCoreLimit();
    }

    public RateLimit getSearchLimit() throws IOException {
        return githubJcabiClient.getSearchLimit();
    }

    public User getUser(String name) throws IOException {
        checkApiLimit("getUser");
        User user = githubJcabiClient.getUser(name);
        decrementRateLimitRemainingCounter("getUser");
        return user;
    }

    public List<Repository> getRepositories(String name) throws IOException {
        checkApiLimit("getRepositories");
        List<Repository> repositoriesList = githubEgitClient.getRepositories(name);
        decrementRateLimitRemainingCounter("getRepositories");
        return repositoriesList;
    }

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        return githubEgitClient.getFollowers(name, limit, offset);
    }

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        return githubEgitClient.getFollowing(name, limit, offset);
    }

    public void checkApiLimit(String methodName) throws GithubRateLimitExceededException, IOException {
        if (methodIsCoreMethod(methodName)) {
            validateCoreRateLimit();
        }
    }

    public void decrementRateLimitRemainingCounter(String methodName)
            throws GithubRateLimitExceededException, IOException {
        if (methodIsCoreMethod(methodName)) {
            decrementCoreRateLimitRemainingCounter();
        }
    }

    private void validateCoreRateLimit() throws GithubRateLimitExceededException, IOException {
        Date now = new Date();

        try {
             com.cezarykluczynski.carmen.model.github.RateLimit rateLimit = rateLimitDAOImpl.getCoreLimit();

            if (rateLimit.getReset().before(now)) {
                /** If limit was just refreshed, we're not bellow it, so it only refresh new and delete old limit. */
                refreshCoreLimit();
                deleteOldCoreLimits();
            } else if (rateLimit.getRemaining() < 10) {
                /** Just to be on the safe side, don't wait for the limit to hit 0. */
                throw new GithubRateLimitExceededException();
            }
        } catch (NullPointerException e) {
            /** If there is no entry yet, just refresh and continue, the limit is not exceeded. */
            refreshCoreLimit();
        }
    }

    private void decrementCoreRateLimitRemainingCounter() {
        rateLimitDAOImpl.decrementRateLimitRemainingCounter();
    }

    private void deleteOldCoreLimits() {
        rateLimitDAOImpl.deleteOldLimits("core");
    }

    private boolean methodIsCoreMethod(String methodName) {
        return methodName.equals("getUser") || methodName.equals("getFollowers") ||
                methodName.equals("getRepositories");
    }

    private void refreshCoreLimit() throws IOException {
        com.cezarykluczynski.carmen.set.github.RateLimit coreRateLimitSet = getCoreLimit();
        rateLimitDAOImpl.create(coreRateLimitSet);
    }

}
