package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.testutil.DateFactory
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.ServerTest
import org.springframework.beans.factory.annotation.Autowired

class RepositoriesClonesDAOImplTest extends IntegrationTest {

    @Autowired
    RepositoriesClonesDAO repositoriesClonesDAO

    @Autowired
    RepositoriesDAO githubRepositoriesDAO

    @Autowired
    RepositoriesDAOImplFixtures githubRepositoriesDAOImplFixtures

    @Autowired
    RepositoriesClonesDAOImplFixtures repositoriesClonesDAOImplFixtures

    def "entity can be created"() {
        given:
        Server server = Mock(Server) {
            getServerId() >> ServerTest.SERVER_ID
            getCloneRoot() >> ServerTest.CLONE_ROOT
        }

        Repository repositoryEntity = new Repository()
        repositoryEntity.setFullName "username/repository"
        githubRepositoriesDAO.create repositoryEntity

        when:
        RepositoryClone repositoryCloneEntity = repositoriesClonesDAO.createStubEntity(server, repositoryEntity)

        then:
        repositoryCloneEntity instanceof RepositoryClone
        repositoryCloneEntity.id != null

        cleanup:
        githubRepositoriesDAOImplFixtures.deleteRepositoryEntity repositoryEntity
        Directory.delete server.getCloneRoot()
    }

    def "finds repository clone with commits to persists when there are clones that were never persisted"() {
        given:
        RepositoryClone repositoryClone1 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2010, 1, 1), DateFactory.of(2010, 2, 2))
        RepositoryClone repositoryClone2 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2013, 1, 1), null)
        RepositoryClone repositoryClone3 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2012, 1, 1), DateFactory.of(2012, 2, 2))
        RepositoryClone repositoryClone4 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2011, 1, 1), null)

        when:
        RepositoryClone repositoryCloneFound = repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist()

        then:
        repositoryCloneFound != null
        repositoryCloneFound.getId() == repositoryClone4.getId()

        cleanup:
        repositoriesClonesDAOImplFixtures.delete repositoryClone1
        repositoriesClonesDAOImplFixtures.delete repositoryClone2
        repositoriesClonesDAOImplFixtures.delete repositoryClone3
        repositoriesClonesDAOImplFixtures.delete repositoryClone4
    }

    def "finds repository clone with commits to persists when there are no clones that were never persisted"() {
        given:
        RepositoryClone repositoryClone1 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2010, 1, 1), DateFactory.of(2010, 2, 2))
        RepositoryClone repositoryClone2 = repositoriesClonesDAOImplFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2011, 1, 1), DateFactory.of(2011, 2, 2))

        when:
        RepositoryClone repositoryCloneFound = repositoriesClonesDAO.findRepositoryCloneWithCommitsToPersist()

        then:
        repositoryCloneFound != null
        repositoryCloneFound.getId() == repositoryClone1.getId()

        cleanup:
        repositoriesClonesDAOImplFixtures.delete repositoryClone1
        repositoriesClonesDAOImplFixtures.delete repositoryClone2
    }

}
