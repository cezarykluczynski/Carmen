package com.cezarykluczynski.carmen.provider.github;

import java.io.IOException;

import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.dao.github.RateLimitDAO;
import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

import java.util.Date;

@Component
public class GithubProvider implements GithubProviderInterface {

    public GithubProvider(
        GithubProviderInterface githubJcabiProvider,
        GithubProviderInterface githubKohsukeProvider,
        GithubProviderInterface githubEgitProvider,
        RateLimitDAO rateLimitDAOImpl
    ) {
        this.githubJcabiProvider = githubJcabiProvider;
        this.githubKohsukeProvider = githubKohsukeProvider;
        this.githubEgitProvider = githubEgitProvider;
        this.rateLimitDAOImpl = rateLimitDAOImpl;
    }

    private GithubProviderInterface githubJcabiProvider;

    private GithubProviderInterface githubKohsukeProvider;

    private GithubProviderInterface githubEgitProvider;

    private RateLimitDAO rateLimitDAOImpl;

    public RateLimit getCoreLimit() throws IOException {
        return githubJcabiProvider.getCoreLimit();
    }

    public RateLimit getSearchLimit() throws IOException {
        return githubJcabiProvider.getSearchLimit();
    }

    public User getUser(String name) throws IOException {
        checkApiLimit("getUser");
        User user = githubJcabiProvider.getUser(name);
        decrementRateLimitRemainingCounter("getUser");
        return user;
    }

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        return githubEgitProvider.getFollowers(name, limit, offset);
    }

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        return githubEgitProvider.getFollowing(name, limit, offset);
    }

    public void checkApiLimit(String methodName) throws GithubRateLimitExceededException, IOException {
        if (methodIsIgnored(methodName)) {
            /**
             *  Accessing /rate_limit endpoint does not count against our rate limit.
             *
             * @see https://developer.github.com/v3/rate_limit/
             */
            return;
        } else if (methodIsCoreMethod(methodName)) {
            validateCoreRateLimit();
        }
    }

    // @After("execution(* carmen.provider.github.GithubProvider.*(..))")
    public void decrementRateLimitRemainingCounter(String methodName) throws GithubRateLimitExceededException, IOException {
        if (methodIsIgnored(methodName)) {
            /**
             *  Accessing /rate_limit endpoint does not count against our rate limit.
             *
             * @see https://developer.github.com/v3/rate_limit/
             */
            return;
        } else if (methodIsCoreMethod(methodName)) {
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

    private boolean methodIsIgnored(String methodName) {
        return methodName == "getCoreLimit" || methodName == "getSearchLimit";
    }

    private boolean methodIsCoreMethod(String methodName) {
        return methodName == "getUser" || methodName == "getFollowers";
    }

    private void refreshCoreLimit() throws IOException {
        com.cezarykluczynski.carmen.set.github.RateLimit coreRateLimitSet = getCoreLimit();
        rateLimitDAOImpl.create(coreRateLimitSet);
    }
}