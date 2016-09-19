package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserFollowingRepository extends JpaRepository<UserFollowing, Long>, UserFollowingRepositoryCustom {

    UserFollowing findOneByUser(User userEntity);

}
