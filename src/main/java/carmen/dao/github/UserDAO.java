package carmen.dao.github;

import carmen.model.github.User;

import java.io.IOException;

public interface UserDAO {

    public User create(carmen.set.github.User user);

    public User update(User userEntity, carmen.set.github.User userSet);

    public void linkFollowerWithFollowee(User follower, User followee);

    public User hydrate(User userEntity, carmen.set.github.User userSet);

    public Object findByLogin(String login);

    public User createOrUpdateRequestedEntity(String username) throws IOException;

    public User createOrUpdateGhostEntity(String username) throws IOException;

    public Object countFound();
}
