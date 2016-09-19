package com.cezarykluczynski.carmen.common.user.model.repository;

import com.cezarykluczynski.carmen.common.user.model.entity.CommonUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonUserRepository extends JpaRepository<CommonUser, Long> {
}
