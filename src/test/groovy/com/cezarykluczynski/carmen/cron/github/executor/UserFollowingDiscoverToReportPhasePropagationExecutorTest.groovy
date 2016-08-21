package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAO
import com.cezarykluczynski.carmen.model.propagations.UserFollowing
import spock.lang.Specification

class UserFollowingDiscoverToReportPhasePropagationExecutorTest extends Specification {

    private static final Integer USER_FOLLOWING_ID = 1
    private static final String DISCOVER_PHASE = "discover"
    private static final String REPORT_PHASE = "report"
    private static final String SLEEP_PHASE = "sleep"

    private UserFollowingDAO userFollowingDAOImpl

    private PendingRequestDAO pendingRequestDAOImpl

    private UserFollowingDiscoverToReportPhasePropagationExecutor userFollowingDiscoverToReportPhasePropagationExecutor

    private UserFollowing userFollowing

    def setup() {
        userFollowingDAOImpl = Mock UserFollowingDAO
        pendingRequestDAOImpl = Mock PendingRequestDAO
        userFollowingDiscoverToReportPhasePropagationExecutor = new UserFollowingDiscoverToReportPhasePropagationExecutor(
                userFollowingDAOImpl, pendingRequestDAOImpl
        )
    }

    def "entity is moved to report phase when there is no more propagations"() {
        given:
        userFollowing = new UserFollowing(id: USER_FOLLOWING_ID, phase: DISCOVER_PHASE)
        userFollowingDAOImpl.findOldestPropagationInDiscoverPhase() >> userFollowing
        pendingRequestDAOImpl.countByPropagationId(USER_FOLLOWING_ID) >> 0

        when:
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        then:
        1 * userFollowingDAOImpl.update(_ as UserFollowing) >> { UserFollowing userFollowingArg ->
            assert userFollowingArg == userFollowing
            assert userFollowingArg.getPhase() == REPORT_PHASE
        }
    }

    def "does not move to report phase when entity is in sleep phase and there is no more propagations"() {
        given:
        userFollowing = new UserFollowing(id: USER_FOLLOWING_ID, phase: SLEEP_PHASE)
        userFollowingDAOImpl.findOldestPropagationInDiscoverPhase() >> userFollowing
        pendingRequestDAOImpl.countByPropagationId(USER_FOLLOWING_ID) >> 1

        when:
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowing.phase == SLEEP_PHASE
        0 * userFollowingDAOImpl.update(*_)
    }

    def "entity is not moved to report phase when there is more propagations"() {
        given:
        userFollowing = new UserFollowing(id: USER_FOLLOWING_ID, phase: DISCOVER_PHASE)
        userFollowingDAOImpl.findOldestPropagationInDiscoverPhase() >> userFollowing
        pendingRequestDAOImpl.countByPropagationId(USER_FOLLOWING_ID) >> 1

        when:
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowing.phase == DISCOVER_PHASE
        0 * userFollowingDAOImpl.update(*_)
    }

}
