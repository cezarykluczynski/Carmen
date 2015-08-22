package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.User;

import java.io.IOException;

public interface UserDAO {

    public User create(com.cezarykluczynski.carmen.set.github.User user);

    public User update(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet);

    public void linkFollowerWithFollowee(User follower, User followee);

    public User hydrate(User userEntity, com.cezarykluczynski.carmen.set.github.User userSet);

    public Object findByLogin(String login);

    public User createOrUpdateRequestedEntity(String username) throws IOException;

    public User createOrUpdateGhostEntity(String username) throws IOException;

    public Object countFound();

    public User findUserInReportFollowersFolloweesPhase() throws IOException;

    public Integer countFollowers(User user);

    public Integer countFollowing(User user);

    public Integer countFollowersFollowing(User user);

}
