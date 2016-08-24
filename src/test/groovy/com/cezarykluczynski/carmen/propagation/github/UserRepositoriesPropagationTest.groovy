package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAOImplFixtures
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.Repositories
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoriesPropagationTest extends IntegrationTest {

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private RepositoriesDAO propagationsRepositoriesDAOImpl

    @Autowired
    private UserRepositoriesPropagation userRepositoriesPropagation

    @Autowired
    private RepositoriesDAOImplFixtures propagationsRepositoriesDAOImplFixtures

    @Autowired
    private PendingRequestDAO apiqueuePendingRequestDAOImpl

    private User userEntity

    private Repositories repositoriesEntity

    def "propagates not found entity"() {
        given:
        userEntity = githubUserDAOImplFixtures.createNotFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        when:
        userRepositoriesPropagation.propagate()

        then:
        propagationsRepositoriesDAOImpl.findByUser(userEntity) == null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "propagates found entity"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        when:
        userRepositoriesPropagation.propagate()
        repositoriesEntity = propagationsRepositoriesDAOImpl.findByUser(userEntity)

        then:
        repositoriesEntity instanceof Repositories
        repositoriesEntity.user.id == userEntity.id
        findPendingRequestEntity() instanceof PendingRequest

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "propagates found entity that is already propagated"() {
        given:
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        userRepositoriesPropagation.setUserEntity userEntity

        when:
        userRepositoriesPropagation.propagate()
        repositoriesEntity = propagationsRepositoriesDAOImpl.findByUser(userEntity)
        userRepositoriesPropagation.propagate()
        propagationsRepositoriesDAOImplFixtures.deleteRepositoriesEntity repositoriesEntity

        then:
        propagationsRepositoriesDAOImpl.findByUser(userEntity) == null

        cleanup:
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    private PendingRequest findPendingRequestEntity() {
        PendingRequest pendingRequestEntityFound = null
        List<PendingRequest> pendingRequestEntitiesList = apiqueuePendingRequestDAOImpl.findByUser(userEntity)
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
