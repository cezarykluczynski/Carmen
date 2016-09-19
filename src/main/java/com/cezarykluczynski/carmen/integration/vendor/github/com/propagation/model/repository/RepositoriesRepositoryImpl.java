package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;

@Service
public class RepositoriesRepositoryImpl extends AbstractPropagationRepositoryImpl<Repositories>
        implements RepositoriesRepositoryCustom {

    @Autowired
    private RepositoriesRepository repositoriesRepository;

    @Autowired
    public RepositoriesRepositoryImpl(EntityManager entityManager) {
        super(entityManager, Repositories.class);
    }

    @Override
    @Transactional
    public Repositories findOldestPropagationInSleepPhase() {
        return findOldestPropagationInPhase("sleep");
    }

    @Override
    @Transactional
    public Repositories create(User userEntity) {
        Repositories repositoriesEntity = new Repositories();

        repositoriesEntity.setUser(userEntity);
        repositoriesEntity.setPhase("refresh");
        repositoriesEntity.setUpdated(new Date());

        return repositoriesRepository.save(repositoriesEntity);
    }

    @Override
    public void moveToRefreshPhase(Repositories repositoriesEntity) {
        repositoriesEntity.setUpdated(new Date());
        repositoriesEntity.setPhase("refresh");
        repositoriesRepository.save(repositoriesEntity);
    }

    @Override
    public void moveToSleepPhaseUsingUserEntity(User userEntity) {
        Repositories repositoriesEntity = repositoriesRepository.findOneByUser(userEntity);
        repositoriesEntity.setPhase("sleep");
        repositoriesRepository.save(repositoriesEntity);
    }
}
