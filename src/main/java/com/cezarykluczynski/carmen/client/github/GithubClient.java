package com.cezarykluczynski.carmen.client.github;

import com.cezarykluczynski.carmen.common.util.pagination.dto.Slice;
import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager;
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO;
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.model.repository.RateLimitRepository;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;
import com.cezarykluczynski.carmen.set.github.UserDTO;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class GithubClient implements GithubClientInterface {

    private GithubClientInterface githubJcabiClient;

    private GithubClientInterface githubKohsukeClient;

    private GithubClientInterface githubEgitClient;

    private RateLimitRepository rateLimitRepository;


    public GithubClient(GithubClientInterface githubJcabiClient, GithubClientInterface githubKohsukeClient,
        GithubClientInterface githubEgitClient, RateLimitRepository rateLimitRepository) {
        this.githubJcabiClient = githubJcabiClient;
        this.githubKohsukeClient = githubKohsukeClient;
        this.githubEgitClient = githubEgitClient;
        this.rateLimitRepository = rateLimitRepository;
    }

    public RateLimitDTO getCoreLimit() throws IOException {
        return githubJcabiClient.getCoreLimit();
    }

    public RateLimitDTO getSearchLimit() throws IOException {
        return githubJcabiClient.getSearchLimit();
    }

    public UserDTO getUser(String name) throws IOException {
        checkApiLimit("getUser");
        UserDTO userDTO = githubJcabiClient.getUser(name);
        decrementRateLimitRemainingCounter("getUser");
        return userDTO;
    }

    public List<RepositoryDTO> getRepositories(String name) throws IOException {
        checkApiLimit("getRepositories");
        List<RepositoryDTO> repositoriesList = githubEgitClient.getRepositories(name);
        decrementRateLimitRemainingCounter("getRepositories");
        return repositoriesList;
    }

    public Slice<UserDTO> getFollowers(String name, Pager pager) throws IOException {
        return githubEgitClient.getFollowers(name, pager);
    }

    public Slice<UserDTO> getFollowing(String name, Pager pager) throws IOException {
        return githubEgitClient.getFollowing(name, pager);
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
             com.cezarykluczynski.carmen.model.github.RateLimit rateLimit = rateLimitRepository.getCoreLimit();

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
        rateLimitRepository.decrementRateLimitRemainingCounter();
    }

    private void deleteOldCoreLimits() {
        rateLimitRepository.deleteOldLimits("core");
    }

    private boolean methodIsCoreMethod(String methodName) {
        return methodName.equals("getUser") || methodName.equals("getFollowers") ||
                methodName.equals("getRepositories");
    }

    private void refreshCoreLimit() throws IOException {
        RateLimitDTO coreRateLimitSet = getCoreLimit();
        rateLimitRepository.create(coreRateLimitSet);
    }

}
