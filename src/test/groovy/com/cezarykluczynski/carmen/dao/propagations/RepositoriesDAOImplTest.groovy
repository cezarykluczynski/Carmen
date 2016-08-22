package com.cezarykluczynski.carmen.dao.propagations

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.Repositories
import org.springframework.beans.factory.annotation.Autowired

class RepositoriesDAOImplTest extends IntegrationTest {

    @Autowired
    private RepositoriesDAO propagationsRepositoriesDAOImpl

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private RepositoriesDAOImplFixtures propagationsRepositoriesDAOImplFixtures

    def "entity is found by user"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        propagationsRepositoriesDAOImplFixtures.createRepositoriesEntityUsingUserEntity(userEntity)

        when:
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser userEntity

        then:
        repositoriesEntityFound != null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "entity is created"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()

        when:
        propagationsRepositoriesDAOImpl.create(userEntity)
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser(userEntity)

        then:
        repositoriesEntityFound != null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "entity is updated"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.create(userEntity)
        repositoriesEntity.setPhase "sleep"

        when:
        propagationsRepositoriesDAOImpl.update repositoriesEntity
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findByUser(userEntity)

        then:
        repositoriesEntityFound.getPhase() == "sleep"

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "entity is deleted"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.create(userEntity)

        when:
        propagationsRepositoriesDAOImpl.delete repositoriesEntity

        then:
        propagationsRepositoriesDAOImpl.findById(repositoriesEntity.getId()) == null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "existing entity is found by id"() {
        given:
        User userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = propagationsRepositoriesDAOImpl.create(userEntity)

        when:
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findById(repositoriesEntity.getId())

        then:
        repositoriesEntityFound != null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "non-existing entity is not found by id"() {
        when:
        Repositories repositoriesEntityFound = propagationsRepositoriesDAOImpl.findById 2147483647

        then:
        repositoriesEntityFound == null
    }

}
