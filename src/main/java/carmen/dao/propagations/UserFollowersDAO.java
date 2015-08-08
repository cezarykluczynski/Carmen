package carmen.dao.propagations;

import carmen.model.propagations.UserFollowers;
import carmen.model.github.User;

import java.util.List;

public interface UserFollowersDAO {

    public List<UserFollowers> findByUser(User userEntity);

}
