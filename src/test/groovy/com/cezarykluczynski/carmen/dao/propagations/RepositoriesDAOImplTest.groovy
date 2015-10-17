package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImpl
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.model.propagations.Repositories
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.fixture.org.hibernate.SessionFactoryFixtures

import org.testng.annotations.Test
import org.testng.Assert

import org.joda.time.DateTimeConstants

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class RepositoriesDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    RepositoriesDAOImpl propagationsRepositoriesDAOImpl

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    RepositoriesDAOImplFixtures propagationsRepositoriesDAOImplFixtures

    @Autowired
    SessionFactoryFixtures sessionFactoryFixtures

    @Autowired
    private SessionFactory sessionFactory

    @Test
    void findByUser() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = propagationsRepositoriesDAOImplFixtures
            .createRepositoriesEntityUsingUserEntityAndPhase(userEntity)

        // exercise
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser userEntity

        // assertion
        Assert.assertTrue repositoriesEntityFound instanceof Repositories

        // teardown
        propagationsRepositoriesDAOImplFixtures.deleteRepositoriesEntity repositoriesEntityFound
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void create() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        // exercise
        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.create(userEntity)

        // assertion
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser(userEntity)
        Assert.assertTrue repositoriesEntityFound instanceof Repositories
        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
        propagationsRepositoriesDAOImplFixtures.deleteRepositoriesEntity repositoriesEntity
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
        propagationsRepositoriesDAOImplFixtures.deleteRepositoriesEntity repositoriesEntity
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
        propagationsRepositoriesDAOImplFixtures.deleteRepositoriesEntity repositoriesEntity
    }

    @Test
    void findByIdNonExistingEntity() {
        // exercise
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findById 2147483647

        // assertion
        Assert.assertNull repositoriesEntityFound
    }

}
