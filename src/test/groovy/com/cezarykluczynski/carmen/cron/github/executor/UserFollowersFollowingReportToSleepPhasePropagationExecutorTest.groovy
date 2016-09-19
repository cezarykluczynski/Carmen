package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository
import com.cezarykluczynski.carmen.propagation.github.UserFollowersFollowingReportToSleepPhasePropagation
import spock.lang.Specification

class UserFollowersFollowingReportToSleepPhasePropagationExecutorTest extends Specification {

    private UserRepository userRepository

    private UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    private UserFollowersFollowingReportToSleepPhasePropagationExecutor userFollowersFollowingReportToSleepPhasePropagationExecutor

    private User userEntity

    def setup() {
        userEntity = new User()
        userRepository = Mock UserRepository
        propagationUserFollowersFollowingReportToSleepPhase = Mock UserFollowersFollowingReportToSleepPhasePropagation
        userFollowersFollowingReportToSleepPhasePropagationExecutor = new UserFollowersFollowingReportToSleepPhasePropagationExecutor(
                userRepository, propagationUserFollowersFollowingReportToSleepPhase
        )
    }

    def "existing entity is used for propagation"() {
        given:
        userRepository.findUserInReportFollowersFolloweesPhase() >> userEntity

        when:
        userFollowersFollowingReportToSleepPhasePropagationExecutor.run()

        then:
        1 * propagationUserFollowersFollowingReportToSleepPhase.setUserEntity(userEntity)
        1 * propagationUserFollowersFollowingReportToSleepPhase.propagate()
    }

    def "null entity does not trigger propagation"() {
        given:
        userRepository.findUserInReportFollowersFolloweesPhase() >> null

        when:
        userFollowersFollowingReportToSleepPhasePropagationExecutor.run()

        then:
        0 * propagationUserFollowersFollowingReportToSleepPhase._
    }

}
