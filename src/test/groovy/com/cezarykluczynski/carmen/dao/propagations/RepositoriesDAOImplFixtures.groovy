package com.cezarykluczynski.carmen.dao.propagations

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.Repositories
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImpl

@Component
class RepositoriesDAOImplFixtures {

    @Autowired
    RepositoriesDAOImpl propagationsUserFollowersDAOImpl

    public Repositories createRepositoriesEntityUsingUserEntityAndPhase(User userEntity) {
        return propagationsUserFollowersDAOImpl.create(userEntity)
    }

    public void deleteRepositoriesEntity(Repositories repositoriesEntity) {
        propagationsUserFollowersDAOImpl.delete repositoriesEntity
    }

}
