package com.cezarykluczynski.carmen.dao.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.set.github.Repository as RepositorySet
import org.springframework.beans.factory.annotation.Autowired

class RepositoriesDAOImplTest extends IntegrationTest {

    @Autowired
    private RepositoriesDAO githubRepositoriesDAOImpl

    @Autowired
    private RepositoriesDAOImplFixtures githubRepositoriesDAOImplFixtures

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    private User userEntity

    def cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "entity is found using user entity"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity
        githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity

        when:
        List<Repository> repositoriesEntitiesList = githubRepositoriesDAOImpl.findByUser userEntity

        then:
        repositoriesEntitiesList.size() == 2
    }

    def "repositories are refreshed"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repository repositoryEntity1 =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity
        Repository repositoryEntity2 =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity

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
        githubRepositoriesDAOImpl.refresh userEntity, repositoriesSetList

        List<Repository> repositoriesEntitiesList = githubRepositoriesDAOImpl.findByUser userEntity

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
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repository repositoryEntity =
            githubRepositoriesDAOImplFixtures.createRandomEntityUsingUserEntity userEntity

        when:
        githubRepositoriesDAOImpl.delete repositoryEntity
        List<Repository> repositoriesEntitiesListAfter = githubRepositoriesDAOImpl.findByUser userEntity

        then:
        repositoriesEntitiesListAfter.size() == 0
    }

}
