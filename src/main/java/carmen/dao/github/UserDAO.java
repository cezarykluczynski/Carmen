package carmen.dao.github;

import carmen.model.github.User;

import java.io.IOException;

public interface UserDAO {

    public User create(carmen.set.github.User user);

    public User update(User userEntity, carmen.set.github.User userSet);

    public User hydrate(User userEntity, carmen.set.github.User userSet);

    public Object findByLogin(String login);

    public User createOrUpdate(String username) throws IOException;
}