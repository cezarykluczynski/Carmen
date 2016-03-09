package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.propagations.Repositories;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserRepositoriesPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    private RepositoriesDAO propagationsRepositoriesDAOImpl;

    private PendingRequestFactory pendingRequestFactory;

    private User userEntity;

    @Autowired
    public UserRepositoriesPropagation(RepositoriesDAO propagationsRepositoriesDAOImpl,
                                       PendingRequestFactory pendingRequestFactory) {
        this.propagationsRepositoriesDAOImpl = propagationsRepositoriesDAOImpl;
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

        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.findByUser(userEntity);

        if (repositoriesEntity == null) {
            createRefreshPhase();
        }
    }

    private void createRefreshPhase() {
        Propagation propagationEntity = propagationsRepositoriesDAOImpl.create(userEntity);
        pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(propagationEntity);
    }

}
