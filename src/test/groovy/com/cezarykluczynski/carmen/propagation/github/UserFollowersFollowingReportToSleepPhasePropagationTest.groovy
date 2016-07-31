package com.cezarykluczynski.carmen.propagation.github

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.github.UserDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImplFixtures
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImplFixtures
import com.cezarykluczynski.carmen.model.cassandra.carmen.FollowersAndFollowees
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import com.cezarykluczynski.carmen.repository.carmen.FollowersAndFolloweesRepository
import org.springframework.beans.factory.annotation.Autowired

class UserFollowersFollowingReportToSleepPhasePropagationTest extends IntegrationTest {

    @Autowired
    private UserDAOImplFixtures githubUserDAOImplFixtures

    @Autowired
    private UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    @Autowired
    private FollowersAndFolloweesRepository followersAndFolloweesRepository

    @Autowired
    private UserFollowersDAO propagationsUserFollowersDAOImpl

    @Autowired
    private UserFollowersDAOImplFixtures propagationsUserFollowersDAOImplFixtures

    @Autowired
    private UserFollowingDAO propagationsUserFollowingDAOImpl

    @Autowired
    private UserFollowingDAOImplFixtures propagationsUserFollowingDAOImplFixtures

    private User userEntity

    private UserFollowers userFollowersEntity

    private UserFollowing userFollowingEntity

    def setup() {
        userEntity = githubUserDAOImplFixtures.createFoundRequestedUserEntity()
        propagationUserFollowersFollowingReportToSleepPhase.setUserEntity userEntity
        userFollowersEntity = propagationsUserFollowersDAOImplFixtures
            .createUserFollowersEntityUsingUserEntityAndPhase userEntity, "report"
        userFollowingEntity = propagationsUserFollowingDAOImplFixtures
            .createUserFollowingEntityUsingUserEntityAndPhase userEntity, "report"
    }

    def cleanup() {
        githubUserDAOImplFixtures.deleteUserEntity userEntity
    }

    def "propagates non-existing followers and followees entity"() {
        when:
        propagationUserFollowersFollowingReportToSleepPhase.propagate()

        then:
        propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId()).getPhase() == "sleep"
        propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId()).getPhase() == "sleep"
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
        propagationsUserFollowersDAOImpl.findById(userFollowersEntity.getId()).getPhase() == "sleep"
        propagationsUserFollowingDAOImpl.findById(userFollowingEntity.getId()).getPhase() == "sleep"
        followersAndFolloweesRepository.findByUserId(userEntity.getId()) instanceof FollowersAndFollowees
    }

}
