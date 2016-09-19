package com.cezarykluczynski.carmen.cron.model.repository

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.util.DateTimeConstants
import com.google.common.collect.Maps
import org.springframework.beans.factory.annotation.Autowired

class PendingRequestRepositoryTest extends IntegrationTest {

    @Autowired
    private PendingRequestRepository pendingRequestRepository

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private PendingRequestRepositoryFixtures pendingRequestRepositoryFixtures

    @Autowired
    private UserFollowersRepositoryFixtures userFollowersRepositoryFixtures

    def "finds pending requests by user"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntity(userEntity)

        when:
        List<PendingRequest> pendingRequestEntitiesList = pendingRequestRepository.findAllByUser userEntity

        then:
        pendingRequestEntitiesList[0].id == pendingRequestEntity.id
        pendingRequestEntitiesList.size() == 1

        cleanup:
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "creates pending request"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepositoryFixtures
                .createUserFollowersEntityUsingUserEntity userEntity
        HashMap<String, Object> emptyParams = Maps.newHashMap()

        when:
        PendingRequest pendingRequestEntity = pendingRequestRepository.create(
            "RandomExecutor",
            userEntity,
            emptyParams,
            emptyParams,
            emptyParams,
            userFollowersEntity,
            0
        )

        then:
        pendingRequestEntity.id != null
        pendingRequestEntity.getPropagationId() == userFollowersEntity.getId()

        cleanup:
        userFollowersRepositoryFixtures.deleteUserFollowersEntity userFollowersEntity
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "finds most important pending request based on priority"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntityWith101Priority =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 101
        PendingRequest pendingRequestEntityWith102Priority =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 102
        PendingRequest pendingRequestEntityWith100Priority =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntityAndPriority userEntity, 100

        when:
        PendingRequest pendingRequestEntityMostImportant = pendingRequestRepository
                .findMostImportantPendingRequest()

        then:
        pendingRequestEntityMostImportant.id == pendingRequestEntityWith102Priority.id

        cleanup:
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntityWith101Priority
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntityWith100Priority
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntityWith102Priority
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "updates executor"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntity userEntity
        pendingRequestEntity.setExecutor "ExecutorForUpdateTest"

        when:
        pendingRequestRepository.save pendingRequestEntity

        then:
        pendingRequestRepository.findOne(pendingRequestEntity.getId()).getExecutor() == "ExecutorForUpdateTest"

        cleanup:
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "deletes entity"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntity userEntity

        Long pendingRequestEntityId = pendingRequestEntity.getId()

        when:
        pendingRequestRepository.delete pendingRequestEntity

        then:
        pendingRequestRepository.findOne(pendingRequestEntityId) == null

        cleanup:
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "counts pending request by propagation id"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        UserFollowers userFollowersEntity = userFollowersRepositoryFixtures
                .createUserFollowersEntityUsingUserEntity userEntity
        PendingRequest pendingRequestEntity1 = pendingRequestRepositoryFixtures
                .createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(userEntity, userFollowersEntity)
        PendingRequest pendingRequestEntity2 = pendingRequestRepositoryFixtures
                .createPendingRequestEntityUsingUserEntityAndUserFollowersEntity(userEntity, userFollowersEntity)

        when:
        Long count = pendingRequestRepository.countByPropagationId userFollowersEntity.getId()

        then:
        count == 2L

        cleanup:
        userFollowersRepositoryFixtures.deleteUserFollowersEntity userFollowersEntity
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntity1
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntity2
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "postpones pending request"() {
        given:
        User userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        PendingRequest pendingRequestEntity =
            pendingRequestRepositoryFixtures.createPendingRequestEntityUsingUserEntity userEntity
        pendingRequestEntity.setUpdated new Date()
        pendingRequestRepository.save pendingRequestEntity

        when:
        pendingRequestRepository.postponeRequest(pendingRequestEntity, DateTimeConstants.MILLISECONDS_IN_MINUTE)
        PendingRequest pendingRequestEntityFound = pendingRequestRepository.findOne(pendingRequestEntity.getId())

        then:
        pendingRequestEntityFound.updated.time == pendingRequestEntity.updated.time

        cleanup:
        pendingRequestRepositoryFixtures.deletePendingRequestEntity pendingRequestEntity
        userRepositoryFixtures.deleteUserEntity userEntity
    }

}
