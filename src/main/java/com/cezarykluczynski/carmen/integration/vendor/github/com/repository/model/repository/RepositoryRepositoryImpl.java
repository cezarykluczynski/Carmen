package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryRepositoryImpl implements RepositoryRepositoryCustom {

    @Autowired
    private RepositoryRepository repositoryRepository;

    @Autowired
    private RepositoryRepositoryRefresherDelegate repositoryRepositoryRefresherDelegate;

    public RepositoryRepositoryImpl() {
    }

    RepositoryRepositoryImpl(RepositoryRepositoryRefresherDelegate repositoryRepositoryRefresherDelegate,
                                    RepositoryRepository repositoryRepository) {
        this.repositoryRepositoryRefresherDelegate = repositoryRepositoryRefresherDelegate;
        this.repositoryRepository = repositoryRepository;
    }

    @Override
    public void refresh(User userEntity, List<RepositoryDTO> repositoriesSetList) {
        List<com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository>
                repositoriesListExisting = repositoryRepository.findByUser(userEntity);
        repositoryRepositoryRefresherDelegate.refresh(userEntity, repositoriesSetList, repositoriesListExisting);
    }
}
