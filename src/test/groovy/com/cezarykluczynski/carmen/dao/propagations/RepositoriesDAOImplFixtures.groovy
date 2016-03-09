package com.cezarykluczynski.carmen.dao.propagations

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.Repositories

class RepositoriesDAOImplFixtures {

    private RepositoriesDAO propagationsUserFollowersDAOImpl

    public RepositoriesDAOImplFixtures(RepositoriesDAO propagationsUserFollowersDAOImpl) {
        this.propagationsUserFollowersDAOImpl = propagationsUserFollowersDAOImpl
    }

    public Repositories createRepositoriesEntityUsingUserEntity(User userEntity) {
        return propagationsUserFollowersDAOImpl.create(userEntity)
    }

    public void deleteRepositoriesEntity(Repositories repositoriesEntity) {
        propagationsUserFollowersDAOImpl.delete repositoriesEntity
    }

}
