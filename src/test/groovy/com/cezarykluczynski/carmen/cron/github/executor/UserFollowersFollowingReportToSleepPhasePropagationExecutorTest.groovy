package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.dao.github.UserDAO
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.propagation.github.UserFollowersFollowingReportToSleepPhasePropagation
import spock.lang.Specification

class UserFollowersFollowingReportToSleepPhasePropagationExecutorTest extends Specification {

    private UserDAO userDAO

    private UserFollowersFollowingReportToSleepPhasePropagation propagationUserFollowersFollowingReportToSleepPhase

    private UserFollowersFollowingReportToSleepPhasePropagationExecutor userFollowersFollowingReportToSleepPhasePropagationExecutor

    private User userEntity

    def setup() {
        userEntity = new User()
        userDAO = Mock UserDAOImpl
        propagationUserFollowersFollowingReportToSleepPhase = Mock UserFollowersFollowingReportToSleepPhasePropagation
        userFollowersFollowingReportToSleepPhasePropagationExecutor = new UserFollowersFollowingReportToSleepPhasePropagationExecutor(
                userDAO, propagationUserFollowersFollowingReportToSleepPhase
        )
    }

    def "existing entity is used for propagation"() {
        given:
        userDAO.findUserInReportFollowersFolloweesPhase() >> userEntity

        when:
        userFollowersFollowingReportToSleepPhasePropagationExecutor.run()

        then:
        1 * propagationUserFollowersFollowingReportToSleepPhase.setUserEntity(userEntity)
        1 * propagationUserFollowersFollowingReportToSleepPhase.propagate()
    }

    def "null entity does not trigger propagation"() {
        given:
        userDAO.findUserInReportFollowersFolloweesPhase() >> null

        when:
        userFollowersFollowingReportToSleepPhasePropagationExecutor.run()

        then:
        0 * propagationUserFollowersFollowingReportToSleepPhase._
    }

}
