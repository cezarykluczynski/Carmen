package com.cezarykluczynski.carmen.client.github;

import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.set.github.UserDTO;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;
import org.kohsuke.github.GitHub;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class GithubKohsukeClient implements GithubClientInterface {

    @Autowired
    public GithubKohsukeClient(GitHub github) {
        this.github = github;
    }

    private GitHub github;

    public RateLimitDTO getCoreLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public RateLimitDTO getSearchLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public UserDTO getUser(String name) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public List<Repository> getRepositories(String login) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public PaginationAwareArrayList<UserDTO> getFollowers(String name, Integer limit, Integer offset)
            throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public PaginationAwareArrayList<UserDTO> getFollowing(String name, Integer limit, Integer offset)
            throws IOException {
        throw new IOException("Implemented in different provider.");
    }

}
