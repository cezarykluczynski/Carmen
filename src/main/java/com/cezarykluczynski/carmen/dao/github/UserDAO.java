package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.User;

import java.io.IOException;

public interface UserDAO {

    User create(com.cezarykluczynski.carmen.set.github.User user);

    User create(User userEntity);

    User update(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet);

    User update(User userEntity);

    void delete(User userEntity);

    void linkFollowerWithFollowee(User follower, User followee);

    User findByLogin(String login);

    User createOrUpdateRequestedEntity(String username) throws IOException;

    User createOrUpdateGhostEntity(String username) throws IOException;

    // TODO: make the following two into one method
    User findById(Integer userId);

    User findById(Long userId);

    Object countFound();

    User findUserInReportFollowersFolloweesPhase() throws IOException;

    Integer countFollowers(User user);

    Integer countFollowees(User user);

    Integer countFollowersFollowing(User user);

    Integer findHighestGitHubUserId();

}
