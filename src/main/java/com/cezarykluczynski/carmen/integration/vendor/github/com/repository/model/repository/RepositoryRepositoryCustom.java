package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;

import java.util.List;

public interface RepositoryRepositoryCustom {

    void refresh(User userEntity, List<RepositoryDTO> repositoriesList);

}
