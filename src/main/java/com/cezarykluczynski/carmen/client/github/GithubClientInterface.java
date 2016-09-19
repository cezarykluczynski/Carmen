package com.cezarykluczynski.carmen.client.github;

import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.set.github.UserDTO;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

import java.io.IOException;
import java.util.List;

public interface GithubClientInterface {

    RateLimitDTO getCoreLimit() throws IOException;

    RateLimitDTO getSearchLimit() throws IOException;

    UserDTO getUser(String name) throws IOException;

    List<Repository> getRepositories(String login) throws IOException;

    PaginationAwareArrayList<UserDTO> getFollowers(String name, Integer limit, Integer offset) throws IOException;

    PaginationAwareArrayList<UserDTO> getFollowing(String name, Integer limit, Integer offset) throws IOException;

}
