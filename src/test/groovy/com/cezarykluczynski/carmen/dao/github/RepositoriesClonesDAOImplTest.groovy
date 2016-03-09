package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.ServerTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert;
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

}
