package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAO
import com.cezarykluczynski.carmen.model.propagations.UserFollowers
import spock.lang.Specification

class UserFollowersDiscoverToReportPhasePropagationExecutorTest extends Specification {

    private static final Integer USER_FOLLOWERS_ID = 1
    private static final String DISCOVER_PHASE = "discover"
    private static final String REPORT_PHASE = "report"
    private static final String SLEEP_PHASE = "sleep"

    private UserFollowersDAO userFollowersDAOImpl

    private PendingRequestDAO pendingRequestDAOImpl

    private UserFollowersDiscoverToReportPhasePropagationExecutor userFollowersDiscoverToReportPhasePropagationExecutor

    private UserFollowers userFollowers

    def setup() {
        userFollowersDAOImpl = Mock UserFollowersDAO
        pendingRequestDAOImpl = Mock PendingRequestDAO
        userFollowersDiscoverToReportPhasePropagationExecutor = new UserFollowersDiscoverToReportPhasePropagationExecutor(
                userFollowersDAOImpl, pendingRequestDAOImpl
        )
    }

    def "entity is moved to report phase when there is no more propagations"() {
        given:
        userFollowers = new UserFollowers(id: USER_FOLLOWERS_ID, phase: DISCOVER_PHASE)
        userFollowersDAOImpl.findOldestPropagationInDiscoverPhase() >> userFollowers
        pendingRequestDAOImpl.countByPropagationId(USER_FOLLOWERS_ID) >> 0

        when:
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        then:
        1 * userFollowersDAOImpl.update(_ as UserFollowers) >> { UserFollowers userFollowersArg ->
            assert userFollowersArg == userFollowers
            assert userFollowersArg.getPhase() == REPORT_PHASE
        }
    }

    def "does not move to report phase when entity is in sleep phase and there is no more propagations"() {
        given:
        userFollowers = new UserFollowers(id: USER_FOLLOWERS_ID, phase: SLEEP_PHASE)
        userFollowersDAOImpl.findOldestPropagationInDiscoverPhase() >> userFollowers
        pendingRequestDAOImpl.countByPropagationId(USER_FOLLOWERS_ID) >> 1

        when:
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowers.phase == SLEEP_PHASE
        0 * userFollowersDAOImpl.update(*_)
    }

    def "entity is not moved to report phase when there is more propagations"() {
        given:
        userFollowers = new UserFollowers(id: USER_FOLLOWERS_ID, phase: DISCOVER_PHASE)
        userFollowersDAOImpl.findOldestPropagationInDiscoverPhase() >> userFollowers
        pendingRequestDAOImpl.countByPropagationId(USER_FOLLOWERS_ID) >> 1

        when:
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowers.phase == DISCOVER_PHASE
        0 * userFollowersDAOImpl.update(*_)
    }

}
