package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.set.github.UserDTO;

import java.io.IOException;

public interface UserRepositoryCustom {

    User create(UserDTO userDTO);

    User update(User userEntity, UserDTO userDTOSet);

    void linkFollowerWithFollowee(User follower, User followee);

    User createOrUpdateRequestedEntity(String username) throws IOException;

    User createOrUpdateGhostEntity(String username) throws IOException;

    Integer countFollowers(User user);

    Integer countFollowees(User user);

    Integer countFollowersFollowing(User user);

    Long findHighestGitHubUserId();

    User findUserInReportFollowersFolloweesPhase();

}
