package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.Propagation;
import com.cezarykluczynski.carmen.model.propagations.Repositories;

import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoriesPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    @Autowired
    private UserDAO githubUserDAOImpl;

    @Autowired
    private RepositoriesDAO propagationsRepositoriesDAOImpl;

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl;

    @Autowired
    PendingRequestFactory pendingRequestFactory;

    private User userEntity;

    @Override
    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    public void propagate() {
        if (!userEntity.getFound()) {
            return;
        }

        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.findByUser(userEntity);

        if (repositoriesEntity instanceof Repositories) {
            return;
        }

        createRefreshPhase();
    }

    private void createRefreshPhase() {
        Propagation propagationEntity = propagationsRepositoriesDAOImpl.create(userEntity);
        pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(propagationEntity);
    }

}
