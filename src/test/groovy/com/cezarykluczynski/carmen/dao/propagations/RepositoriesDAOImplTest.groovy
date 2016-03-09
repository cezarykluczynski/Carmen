package com.cezarykluczynski.carmen.dao.propagations

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.propagations.Repositories
import com.cezarykluczynski.carmen.model.github.User
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.Test
import org.testng.Assert

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class RepositoriesDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    RepositoriesDAO propagationsRepositoriesDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    RepositoriesDAOImplFixtures propagationsRepositoriesDAOImplFixtures

    @Test
    void findByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        propagationsRepositoriesDAOImplFixtures.createRepositoriesEntityUsingUserEntity(userEntity)

        // exercise
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser userEntity

        // assertion
        Assert.assertTrue repositoriesEntityFound instanceof Repositories

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        propagationsRepositoriesDAOImpl.create(userEntity)

        // assertion
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser(userEntity)
        Assert.assertTrue repositoriesEntityFound instanceof Repositories
        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void update() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.create(userEntity)
        repositoriesEntity.setPhase "sleep"

        // exercise
        propagationsRepositoriesDAOImpl.update repositoriesEntity

        // assertion
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser(userEntity)
        Assert.assertEquals repositoriesEntityFound.getPhase(), "sleep"

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void delete() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.create(userEntity)

        // exercise
        propagationsRepositoriesDAOImpl.delete repositoriesEntity

        // assertion
        Assert.assertNull propagationsRepositoriesDAOImpl.findById(repositoriesEntity.getId())

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findByIdExistingEntity() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.create(userEntity)

        // exercise
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findById(repositoriesEntity.getId())

        // assertion
        Assert.assertTrue repositoriesEntityFound instanceof Repositories

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void findByIdNonExistingEntity() {
        // exercise
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findById 2147483647

        // assertion
        Assert.assertNull repositoriesEntityFound
    }

}
