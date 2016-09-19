package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.set.github.Repository as RepositorySet
import org.springframework.beans.factory.annotation.Autowired

class RepositoryRepositoryTest extends IntegrationTest {

    @Autowired
    private RepositoryRepository repositoryRepository

    @Autowired
    private RepositoryRepositoryFixtures repositoryRepositoryFixtures

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    private User userEntity

    def cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is found using user entity"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        repositoryRepositoryFixtures.createRandomEntityUsingUserEntity userEntity
        repositoryRepositoryFixtures.createRandomEntityUsingUserEntity userEntity

        when:
        List<Repository> repositoriesEntitiesList = repositoryRepository.findByUser userEntity

        then:
        repositoriesEntitiesList.size() == 2
    }

    def "repositories are refreshed"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        Repository repositoryEntity1 =
            repositoryRepositoryFixtures.createRandomEntityUsingUserEntity userEntity
        Repository repositoryEntity2 =
            repositoryRepositoryFixtures.createRandomEntityUsingUserEntity userEntity

        List<RepositorySet> repositoriesSetList = new ArrayList<RepositorySet>()

        RepositorySet repositorySet2 = new RepositorySet(
            repositoryEntity2.getId(),
            null, null, null, null, null, false, null, null, null, null, null
        )

        RepositorySet repositorySet3 = new RepositorySet(
            repositoryEntity2.getId() + 1,
            null, null, null, null, null, false, null, null, null, null, null
        )

        repositoriesSetList.add repositorySet2
        repositoriesSetList.add repositorySet3

        when:
        repositoryRepository.refresh userEntity, repositoriesSetList

        List<Repository> repositoriesEntitiesList = repositoryRepository.findByUser userEntity

        def preserved = false
        def created = false
        def deleted = true

        for (Repository repositoryEntity in repositoriesEntitiesList) {
            if (repositoryEntity.getId().equals(repositoryEntity1.getId())) {
                deleted = false
            }
            if (repositoryEntity.getId().equals(repositorySet2.getId())) {
                preserved = true
            }
            if (repositoryEntity.getId().equals(repositorySet3.getId())) {
                created = true
            }
        }

        then:
        repositoriesEntitiesList.size() == 2
        created
        preserved
        deleted
    }

    def "repositories are deleted"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        Repository repositoryEntity =
            repositoryRepositoryFixtures.createRandomEntityUsingUserEntity userEntity

        when:
        repositoryRepository.delete repositoryEntity
        List<Repository> repositoriesEntitiesListAfter = repositoryRepository.findByUser userEntity

        then:
        repositoriesEntitiesListAfter.size() == 0
    }

}
