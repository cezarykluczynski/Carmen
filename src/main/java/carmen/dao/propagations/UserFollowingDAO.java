package carmen.dao.propagations;

import carmen.model.propagations.UserFollowing;
import carmen.model.github.User;

import java.util.List;

public interface UserFollowingDAO {

    public List<UserFollowing> findByUser(User userEntity);

}
