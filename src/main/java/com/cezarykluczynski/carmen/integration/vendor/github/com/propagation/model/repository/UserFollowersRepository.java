package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowersRepository extends JpaRepository<UserFollowers, Long>, UserFollowersRepositoryCustom {

    UserFollowers findOneByUser(User userEntity);

}
