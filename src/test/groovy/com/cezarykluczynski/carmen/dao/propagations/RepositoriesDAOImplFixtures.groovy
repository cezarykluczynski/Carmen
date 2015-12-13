package com.cezarykluczynski.carmen.dao.propagations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.Repositories

@Component
class RepositoriesDAOImplFixtures {

    @Autowired
    RepositoriesDAO propagationsUserFollowersDAOImpl

    public Repositories createRepositoriesEntityUsingUserEntity(User userEntity) {
        return propagationsUserFollowersDAOImpl.create(userEntity)
    }

    public void deleteRepositoriesEntity(Repositories repositoriesEntity) {
        propagationsUserFollowersDAOImpl.delete repositoriesEntity
    }

}
