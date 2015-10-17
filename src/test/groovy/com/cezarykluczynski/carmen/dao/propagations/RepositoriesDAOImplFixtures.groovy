package com.cezarykluczynski.carmen.dao.propagations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.Repositories
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImpl

@Component
class RepositoriesDAOImplFixtures {

    @Autowired
    RepositoriesDAOImpl propagationsUserFollowersDao

    public Repositories createRepositoriesEntityUsingUserEntityAndPhase(User userEntity) {
        return propagationsUserFollowersDao.create(userEntity)
    }

    public void deleteRepositoriesEntity(Repositories repositoriesEntity) {
        propagationsUserFollowersDao.delete repositoriesEntity
    }

}
