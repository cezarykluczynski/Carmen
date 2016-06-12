package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.testutil.DateFactory
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.ServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class RepositoriesClonesDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    RepositoriesClonesDAO repositoriesClonesDAO

    @Autowired
    RepositoriesDAO githubRepositoriesDAO

    @Autowired
    RepositoriesDAOImplFixtures githubRepositoriesDAOImplFixtures

    @Autowired
    RepositoriesClonesDAOImplFixtures repositoriesClonesDAOImplFixtures

    @Test
    void entityCanBeCreated() {
        Server server = mock Server.class
        when server.getServerId() thenReturn ServerTest.SERVER_ID
        when server.getCloneRoot() thenReturn ServerTest.CLONE_ROOT

        Repository repositoryEntity = new Repository()
        repositoryEntity.setFullName "username/repository"
        githubRepositoriesDAO.create repositoryEntity

        RepositoryClone repositoryCloneEntity = repositoriesClonesDAO.createStubEntity(server, repositoryEntity)

        Assert.assertTrue repositoryCloneEntity instanceof RepositoryClone
        Assert.assertNotNull repositoryCloneEntity.getId()

        // teardown
        githubRepositoriesDAOImplFixtures.deleteRepositoryEntity repositoryEntity
        Directory.delete server.getCloneRoot()
    }

    @Test
    void "find repository clone with commits to persists when there are clones that were never persisted"() {
        // setup
        RepositoryClone repositoryClone1 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2010, 1, 1), DateFactory.of(2010, 2, 2))
        RepositoryClone repositoryClone2 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2013, 1, 1), null)
        RepositoryClone repositoryClone3 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2012, 1, 1), DateFactory.of(2012, 2, 2))
        RepositoryClone repositoryClone4 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2011, 1, 1), null)

        // exercise
        RepositoryClone repositoryCloneFound = repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist()

        // assertion
        Assert.assertNotNull repositoryCloneFound
        Assert.assertEquals repositoryCloneFound.getId(), repositoryClone4.getId()

        repositoriesClonesDAOImplFixtures.delete repositoryClone1
        repositoriesClonesDAOImplFixtures.delete repositoryClone2
        repositoriesClonesDAOImplFixtures.delete repositoryClone3
        repositoriesClonesDAOImplFixtures.delete repositoryClone4
    }

    @Test
    void "find repository clone with commits to persists when there are no clones that were never persisted"() {
        // setup
        RepositoryClone repositoryClone1 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2010, 1, 1), DateFactory.of(2010, 2, 2))
        RepositoryClone repositoryClone2 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2011, 1, 1), DateFactory.of(2011, 2, 2))

        // exercise
        RepositoryClone repositoryCloneFound = repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist()

        // assertion
        Assert.assertNotNull repositoryCloneFound
        Assert.assertEquals repositoryCloneFound.getId(), repositoryClone1.getId()

        repositoriesClonesDAOImplFixtures.delete repositoryClone1
        repositoriesClonesDAOImplFixtures.delete repositoryClone2
    }

}
