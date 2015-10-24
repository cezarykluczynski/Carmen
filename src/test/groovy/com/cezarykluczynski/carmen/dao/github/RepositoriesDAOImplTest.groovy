package com.cezarykluczynski.carmen.dao.github

import org.hibernate.Session
import org.hibernate.SessionFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests

import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAOImpl
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.set.github.Repository as RepositorySet

import org.testng.annotations.Test
import org.testng.Assert

import java.util.List
import java.util.ArrayList

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class RepositoriesDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    RepositoriesDAOImpl githubRepositoriesDAOImpl

    @Autowired
    RepositoriesDAOImplFixtures githubRepositoriesDAOImplFixtures

    @Autowired
    UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private SessionFactory sessionFactory

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Test
    void findById() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repository repositoryEntity1 =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity
        Repository repositoryEntity2 =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity

        // exercise
        List<Repository> repositoriesEntitiesList = githubRepositoriesDAOImpl.findByUser userEntity

        // assertion
        Assert.assertEquals repositoriesEntitiesList.size(), 2

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void refresh() {
        // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repository repositoryEntity1 =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity
        Repository repositoryEntity2 =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity

        List<RepositorySet> repositoriesSetList = new ArrayList<RepositorySet>()

        RepositorySet repositorySet2 = new RepositorySet(
            repositoryEntity1.getId(),
            null, null, null, null, null, false, null, null, null, null, null
        )

        RepositorySet repositorySet3 = new RepositorySet(
            repositoryEntity1.getId() + 1,
            null, null, null, null, null, false, null, null, null, null, null
        )

        repositoriesSetList.add repositorySet2
        repositoriesSetList.add repositorySet3

        // exercise
        githubRepositoriesDAOImpl.refresh userEntity, repositoriesSetList

        // assertion
        List<Repository> repositoriesEntitiesList = githubRepositoriesDAOImpl.findByUser userEntity
        Assert.assertEquals repositoriesEntitiesList.size(), 2

        def okPreserve = false
        def okCreate = false

        for (Repository repositoryEntity in repositoriesEntitiesList) {
            if (repositoryEntity.getId() == repositoryEntity1.getId()) {
                okPreserve = true
                Assert.assertTrue okPreserve
            }
            if (repositoryEntity.getId() == repositorySet3.getId()) {
                okCreate = true
                Assert.assertTrue okCreate
            }
        }

        if (!okPreserve) {
            Assert.fail "Repository was not preserved."
        }

        if (!okCreate) {
            Assert.fail "Repository was not created."
        }

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    @Test
    void delete() {
         // setup
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repository repositoryEntity =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity

        // assertion
        List<Repository> repositoriesEntitiesListBefore = githubRepositoriesDAOImpl.findByUser userEntity
        Assert.assertEquals repositoriesEntitiesListBefore.size(), 1

        // exercise
        githubRepositoriesDAOImpl.delete repositoryEntity

        // assertion
        List<Repository> repositoriesEntitiesListAfter = githubRepositoriesDAOImpl.findByUser userEntity
        Assert.assertEquals repositoriesEntitiesListAfter.size(), 0

        // teardown
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

}
