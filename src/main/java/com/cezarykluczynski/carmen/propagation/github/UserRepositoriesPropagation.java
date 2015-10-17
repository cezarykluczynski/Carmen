package com.cezarykluczynski.carmen.propagation.github;

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImpl;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.propagations.Repositories;

import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoriesPropagation implements com.cezarykluczynski.carmen.propagation.Propagation {

    @Autowired
    private UserDAOImpl githubUserDAOImpl;

    @Autowired
    private RepositoriesDAOImpl propagationsRepositoriesDAOImpl;

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

        createDiscoverPhase();
    }

    private void createDiscoverPhase() {
        propagationsRepositoriesDAOImpl.create(userEntity);
    }

}
