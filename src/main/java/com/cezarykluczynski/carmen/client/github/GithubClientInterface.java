package com.cezarykluczynski.carmen.client.github;

import com.cezarykluczynski.carmen.common.util.pagination.dto.Slice;
import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager;
import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;
import com.cezarykluczynski.carmen.set.github.UserDTO;

import java.io.IOException;
import java.util.List;

public interface GithubClientInterface {

    String NOT_IMPLEMENTED = "Not implemented.";

    default RateLimitDTO getCoreLimit() throws IOException {
        throw new IOException(NOT_IMPLEMENTED);
    }

    default RateLimitDTO getSearchLimit() throws IOException {
        throw new IOException(NOT_IMPLEMENTED);
    }

    default UserDTO getUser(String name) throws IOException {
        throw new IOException(NOT_IMPLEMENTED);
    }

    default List<RepositoryDTO> getRepositories(String login) throws IOException {
        throw new IOException(NOT_IMPLEMENTED);
    }

    default Slice<UserDTO> getFollowers(String name, Pager pager) throws IOException {
        throw new IOException(NOT_IMPLEMENTED);
    }

    default Slice<UserDTO> getFollowing(String name, Pager pager) throws IOException {
        throw new IOException(NOT_IMPLEMENTED);
    }

}
