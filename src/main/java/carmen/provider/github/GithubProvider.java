package carmen.provider.github;

import java.io.IOException;

import carmen.dao.github.RateLimitDAO;
import carmen.set.github.User;
import carmen.set.github.RateLimit;

import java.util.Date;

public class GithubProvider implements GithubProviderInterface {

    public GithubProvider(GithubProviderInterface githubProvider, RateLimitDAO rateLimitDAOImpl) {
        this.githubProvider = githubProvider;
        this.rateLimitDAOImpl = rateLimitDAOImpl;
    }

    private GithubProviderInterface githubProvider;
    private RateLimitDAO rateLimitDAOImpl;

    public RateLimit getCoreLimit() throws IOException {
        return githubProvider.getCoreLimit();
    }

    public RateLimit getSearchLimit() throws IOException {
        return githubProvider.getSearchLimit();
    }

    public User getUser(String name) throws IOException {
        checkApiLimit("getUser");
        User user = githubProvider.getUser(name);
        decrementRateLimitRemainingCounter("getUser");
        return user;
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
             carmen.model.github.RateLimit rateLimit = rateLimitDAOImpl.getCoreLimit();

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
        return methodName == "getUser";
    }

    private void refreshCoreLimit() throws IOException {
        carmen.set.github.RateLimit coreRateLimitSet = getCoreLimit();
        rateLimitDAOImpl.create(coreRateLimitSet);
    }
}