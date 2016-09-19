package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepositoryFixtures
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepositoryFixtures
import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees
import com.cezarykluczynski.carmen.repository.carmen.FollowersAndFolloweesRepository
import org.springframework.beans.factory.annotation.Autowired

class UserFollowersFollowingReportToSleepPhasePropagationTest extends IntegrationTest {

    @Autowired
    private UserRepositoryFixtures userRepositoryFixtures

    @Autowired
    private UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    @Autowired
    private FollowersAndFolloweesRepository followersAndFolloweesRepository

    @Autowired
    private UserFollowersRepository userFollowersRepository

    @Autowired
    private UserFollowersRepositoryFixtures userFollowersRepositoryFixtures

    @Autowired
    private UserFollowingRepository userFollowingRepository

    @Autowired
    private UserFollowingRepositoryFixtures userFollowingRepositoryFixtures

    private User userEntity

    private UserFollowers userFollowersEntity

    private UserFollowing userFollowingEntity

    def setup() {
        userEntity = userRepositoryFixtures.createFoundRequestedUserEntity()
        propagationUserFollowersFollowingReportToSleepPhase.setUserEntity userEntity
        userFollowersEntity = userFollowersRepositoryFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase userEntity, "report"
        userFollowingEntity = userFollowingRepositoryFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase userEntity, "report"
    }

    def cleanup() {
        userRepositoryFixtures.deleteUserEntity userEntity
    }

    def "propagates non-existing followers and followees entity"() {
        when:
        propagationUserFollowersFollowingReportToSleepPhase.propagate()

        then:
        userFollowersRepository.findOne(userFollowersEntity.getId()).getPhase() == "sleep"
        userFollowingRepository.findOne(userFollowingEntity.getId()).getPhase() == "sleep"
        followersAndFolloweesRepository.findByUserId(userEntity.getId()) instanceof FollowersAndFollowees
    }

    def "propagates existing followers and followees entity"() {
        given:
        FollowersAndFollowees followersAndFolloweesEntity = new FollowersAndFollowees()
        followersAndFolloweesEntity.setId()
        followersAndFolloweesEntity.setUserId userEntity.getId()
        followersAndFolloweesRepository.save followersAndFolloweesEntity

        when:
        propagationUserFollowersFollowingReportToSleepPhase.propagate()

        then:
        userFollowersRepository.findOne(userFollowersEntity.getId()).getPhase() == "sleep"
        userFollowingRepository.findOne(userFollowingEntity.getId()).getPhase() == "sleep"
        followersAndFolloweesRepository.findByUserId(userEntity.getId()) instanceof FollowersAndFollowees
    }

}
