package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoriesRepository extends JpaRepository<Repositories, Long>, RepositoriesRepositoryCustom {

    Repositories findOneByUser(User userEntity);

}
