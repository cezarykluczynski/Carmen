package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoriesPropagationTest extends IntegrationTest {

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private RepositoriesRepository RepositoriesRepository

    @Autowired
    private UserRepositoriesPropagation userRepositoriesPropagation

    @Autowired
    private RepositoriesRepositoryFixtures repositoriesRepositoryFixtures

    @Autowired
    private PendingRequestRepository pendingRequestRepository

    private User userEntity

    private Repositories repositoriesEntity

    def "propagates not found entity"() {
        given:
        userEntity = userRepositoryFixtures.createNotFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        when:
        userRepositoriesPropagation.propagate()

        then:
        RepositoriesRepository.findOneByUser(userEntity) == null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "propagates found entity"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        when:
        userRepositoriesPropagation.propagate()
        repositoriesEntity = RepositoriesRepository.findOneByUser(userEntity)

        then:
        repositoriesEntity instanceof Repositories
        repositoriesEntity.user.id == userEntity.id
        findPendingRequestEntity() instanceof PendingRequest

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "propagates found entity that is already propagated"() {
        given:
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        when:
        userRepositoriesPropagation.propagate()
        repositoriesEntity = RepositoriesRepository.findOneByUser(userEntity)
        userRepositoriesPropagation.propagate()
        repositoriesRepositoryFixtures.deleteRepositoriesEntity repositoriesEntity

        then:
        RepositoriesRepository.findOneByUser(userEntity) == null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    private PendingRequest findPendingRequestEntity() {
        PendingRequest pendingRequestEntityFound = null
        List<PendingRequest> pendingRequestEntitiesList = pendingRequestRepository.findAllByUser(userEntity)
        for(PendingRequest pendingRequestEntity in pendingRequestEntitiesList) {
            if (pendingRequestEntity.getExecutor() == "Repositories") {
                if (pendingRequestEntityFound == null) {
                    pendingRequestEntityFound = pendingRequestEntity
                } else {
                    return null
                }
            }
        }
        return pendingRequestEntityFound
    }

}
