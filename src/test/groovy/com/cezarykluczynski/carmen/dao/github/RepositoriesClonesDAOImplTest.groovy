package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.TomcatServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert;
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml",
    "classpath:spring/fixtures/fixtures.xml"
])
class RepositoriesClonesDAOImplTest extends AbstractTestNGSpringContextTests {

    @Autowired
    RepositoriesClonesDAO repositoriesClonesDAO

    @Autowired
    RepositoriesDAO githubRepositoriesDAO

    @Autowired
    RepositoriesDAOImplFixtures githubRepositoriesDAOImplFixtures

    @Test
    void entityCanBeCreated() {
        Server tomcatServer = mock TomcatServer.class
        when tomcatServer.getServerId() thenReturn "tomcat-test"
        when tomcatServer.getCloneRoot() thenReturn "target/test-storage"

        Repository repositoryEntity = new Repository()
        repositoryEntity.setFullName "username/repository"
        githubRepositoriesDAO.create repositoryEntity

        RepositoryClone repositoryCloneEntity = repositoriesClonesDAO.createStubEntity(tomcatServer, repositoryEntity)

        Assert.assertTrue repositoryCloneEntity instanceof RepositoryClone
        Assert.assertNotNull repositoryCloneEntity.getId()

        // teardown
        githubRepositoriesDAOImplFixtures.deleteRepositoryEntity repositoryEntity
        Directory.delete tomcatServer.getCloneRoot()
    }

}
