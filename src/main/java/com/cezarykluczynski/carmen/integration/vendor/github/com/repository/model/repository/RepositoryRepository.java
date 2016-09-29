package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositoryRepository extends JpaRepository<Repository, Long>, RepositoryRepositoryCustom {

    List<Repository> findByUser(User userEntity);

    void refresh(User userEntity, List<RepositoryDTO> repositoriesList);

    Repository findFirstByRepositoryCloneIsNullAndForkFalse();

}
