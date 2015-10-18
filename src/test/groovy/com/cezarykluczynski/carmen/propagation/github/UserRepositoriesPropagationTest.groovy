package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.Repositories
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration([
        "classpath:spring/database-config.xml",
        "classpath:spring/mvc-core-config.xml",
        "classpath:spring/cron-config.xml",
        "classpath:spring/fixtures/fixtures.xml"
])
class UserRepositoriesPropagationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    RepositoriesDAOImpl propagationsRepositoriesDAOImpl

    @Autowired
    UserRepositoriesPropagation userRepositoriesPropagation

    @Autowired
    RepositoriesDAOImplFixtures propagationsRepositoriesDAOImplFixtures

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDao

    User userEntity

    Repositories repositoriesEntity

    @Test
    void propagateNotFoundEntity() {
        // setup
        userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        // exercise
        userRepositoriesPropagation.propagate()

        // assertion
        Assert.assertNull propagationsRepositoriesDAOImpl.findByUser(userEntity)

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void propagateFoundEntity() {
        // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        // exercise
        userRepositoriesPropagation.propagate()

        // assertion
        repositoriesEntity = propagationsRepositoriesDAOImpl.findByUser(userEntity)
        Assert.assertTrue repositoriesEntity instanceof Repositories
        Assert.assertEquals repositoriesEntity.getUser().getId(), userEntity.getId()

        PendingRequest pendingRequestEntityFound = null
        List<PendingRequest> pendingRequestEntitiesList = apiqueuePendingRequestDao.findByUser userEntity
        for(PendingRequest pendingRequestEntity in pendingRequestEntitiesList) {
            if (pendingRequestEntity.getExecutor() == "Repositories") {
                if (pendingRequestEntityFound == null) {
                    pendingRequestEntityFound = pendingRequestEntity
                } else {
                    Assert.assertTrue false
                }
            }
        }
        Assert.assertTrue pendingRequestEntityFound instanceof PendingRequest

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void propagateFoundEntityAlreadyPropagated() {
            // setup
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        // exercise
        userRepositoriesPropagation.propagate()
        repositoriesEntity = propagationsRepositoriesDAOImpl.findByUser(userEntity)
        userRepositoriesPropagation.propagate()
        propagationsRepositoriesDAOImplFixtures.deleteRepositoriesEntity repositoriesEntity

        // assertion
        Assert.assertNull propagationsRepositoriesDAOImpl.findByUser(userEntity)
    }

}
