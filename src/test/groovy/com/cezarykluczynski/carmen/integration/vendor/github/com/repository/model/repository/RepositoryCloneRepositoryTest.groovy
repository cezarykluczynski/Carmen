package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone
import com.cezarykluczynski.carmen.testutil.DateFactory
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.ServerTest
import org.springframework.beans.factory.annotation.Autowired

class RepositoryCloneRepositoryTest extends IntegrationTest {

    @Autowired
    RepositoryCloneRepository repositoryCloneRepository

    @Autowired
    RepositoryRepository repositoryRepository

    @Autowired
    RepositoryCloneRepositoryFixtures repositoryCloneRepositoryFixtures

    def "entity can be created"() {
        setup:
        Server server = Mock(Server) {
            getServerId() >> ServerTest.SERVER_ID
            getCloneRoot() >> ServerTest.CLONE_ROOT
        }

        Repository repositoryEntity = new Repository()
        repositoryEntity.setFullName "username/repository"
        repositoryEntity = repositoryRepository.save repositoryEntity

        when:
        RepositoryClone repositoryCloneEntity = repositoryCloneRepository.createStubEntity(server, repositoryEntity)

        then:
        repositoryCloneEntity instanceof RepositoryClone
        repositoryCloneEntity.id != null

        cleanup:
        repositoryRepository.delete repositoryEntity
        Directory.delete server.getCloneRoot()
    }

    def "finds repository clone with commits to persists when there are clones that were never persisted"() {
        given:
        RepositoryClone repositoryClone1 = repositoryCloneRepositoryFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2010, 1, 1), DateFactory.of(2010, 2, 2))
        RepositoryClone repositoryClone2 = repositoryCloneRepositoryFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2013, 1, 1), null)
        RepositoryClone repositoryClone3 = repositoryCloneRepositoryFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2012, 1, 1), DateFactory.of(2012, 2, 2))
        RepositoryClone repositoryClone4 = repositoryCloneRepositoryFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2011, 1, 1), null)

        when:
        RepositoryClone repositoryCloneFound = repositoryCloneRepository.findRepositoryCloneWithCommitsToPersist()

        then:
        repositoryCloneFound != null
        repositoryCloneFound.id == repositoryClone4.id

        cleanup:
        repositoryCloneRepositoryFixtures.delete repositoryClone1
        repositoryCloneRepositoryFixtures.delete repositoryClone2
        repositoryCloneRepositoryFixtures.delete repositoryClone3
        repositoryCloneRepositoryFixtures.delete repositoryClone4
    }

    def "finds repository clone with commits to persists when there are no clones that were never persisted"() {
        given:
        RepositoryClone repositoryClone1 = repositoryCloneRepositoryFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2010, 1, 1), DateFactory.of(2010, 2, 2))
        RepositoryClone repositoryClone2 = repositoryCloneRepositoryFixtures
                .createWithUpdatedAndCommitsStatisticsUntil(DateFactory.of(2011, 1, 1), DateFactory.of(2011, 2, 2))

        when:
        RepositoryClone repositoryCloneFound = repositoryCloneRepository.findRepositoryCloneWithCommitsToPersist()

        then:
        repositoryCloneFound != null
        repositoryCloneFound.getId() == repositoryClone1.getId()

        cleanup:
        repositoryCloneRepositoryFixtures.delete repositoryClone1
        repositoryCloneRepositoryFixtures.delete repositoryClone2
    }

}
