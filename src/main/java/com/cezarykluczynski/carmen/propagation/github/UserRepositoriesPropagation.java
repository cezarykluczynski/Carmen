package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepository;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoriesPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    private RepositoriesRepository repositoriesRepository;

    private PendingRequestFactory pendingRequestFactory;

    private User userEntity;

    @Autowired
    public UserRepositoriesPropagation(RepositoriesRepository repositoriesRepository,
            PendingRequestFactory pendingRequestFactory) {
        this.repositoriesRepository = repositoriesRepository;
        this.pendingRequestFactory = pendingRequestFactory;
    }

    @Override
    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public void propagate() {
        if (!userEntity.isFound()) {
            return;
        }

        Repositories repositoriesEntity = repositoriesRepository.findOneByUser(userEntity);

        if (repositoriesEntity == null) {
            createRefreshPhase();
        }
    }

    private void createRefreshPhase() {
        Propagation propagationEntity = repositoriesRepository.create(userEntity);
        pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(propagationEntity);
    }

}
