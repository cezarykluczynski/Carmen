package com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories
import org.springframework.beans.factory.annotation.Autowired

class RepositoriesRepositoryTest extends IntegrationTest {

    @Autowired
    private RepositoriesRepository repositoriesRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private RepositoriesRepositoryFixtures repositoriesRepositoryFixtures

    def "entity is found by user"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        repositoriesRepositoryFixtures.createRepositoriesEntityUsingUserEntity(userEntity)

        when:
        Repositories repositoriesEntityFound = repositoriesRepository.findOneByUser userEntity

        then:
        repositoriesEntityFound != null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is created"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()

        when:
        repositoriesRepository.create(userEntity)
        Repositories repositoriesEntityFound = repositoriesRepository.findOneByUser(userEntity)

        then:
        repositoriesEntityFound != null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is updated"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = repositoriesRepository.create(userEntity)
        repositoriesEntity.setPhase "sleep"

        when:
        repositoriesRepository.save repositoriesEntity
        Repositories repositoriesEntityFound = repositoriesRepository.findOneByUser(userEntity)

        then:
        repositoriesEntityFound.getPhase() == "sleep"

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "entity is deleted"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = repositoriesRepository.create(userEntity)

        when:
        repositoriesRepository.delete repositoriesEntity

        then:
        repositoriesRepository.findOne(repositoriesEntity.getId()) == null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "existing entity is found by id"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        Repositories repositoriesEntity = repositoriesRepository.create(userEntity)

        when:
        Repositories repositoriesEntityFound = repositoriesRepository.findOne(repositoriesEntity.getId())

        then:
        repositoriesEntityFound != null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "non-existing entity is not found by id"() {
        when:
        Repositories repositoriesEntityFound = repositoriesRepository.findOne 2147483647L

        then:
        repositoriesEntityFound == null
    }

}
