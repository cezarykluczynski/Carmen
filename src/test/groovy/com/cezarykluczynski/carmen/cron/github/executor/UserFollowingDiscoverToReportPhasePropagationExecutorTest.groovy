package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowing
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowingRepository
import spock.lang.Specification

class UserFollowingDiscoverToReportPhasePropagationExecutorTest extends Specification {

    private static final Integer USER_FOLLOWING_ID = 1
    private static final String DISCOVER_PHASE = "discover"
    private static final String REPORT_PHASE = "report"
    private static final String SLEEP_PHASE = "sleep"

    private UserFollowingRepository userFollowingRepository

    private PendingRequestRepository pendingRequestRepository

    private UserFollowingDiscoverToReportPhasePropagationExecutor userFollowingDiscoverToReportPhasePropagationExecutor

    private UserFollowing userFollowing

    def setup() {
        userFollowingRepository = Mock UserFollowingRepository
        pendingRequestRepository = Mock PendingRequestRepository
        userFollowingDiscoverToReportPhasePropagationExecutor = new UserFollowingDiscoverToReportPhasePropagationExecutor(
                userFollowingRepository, pendingRequestRepository)
    }

    def "entity is moved to report phase when there is no more propagations"() {
        given:
        userFollowing = new UserFollowing(id: USER_FOLLOWING_ID, phase: DISCOVER_PHASE)
        userFollowingRepository.findOldestPropagationInDiscoverPhase() >> userFollowing
        pendingRequestRepository.countByPropagationId(USER_FOLLOWING_ID) >> 0

        when:
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        then:
        1 * userFollowingRepository.save(_ as UserFollowing) >> { UserFollowing userFollowingArg ->
            assert userFollowingArg == userFollowing
            assert userFollowingArg.getPhase() == REPORT_PHASE
        }
    }

    def "does not move to report phase when entity is in sleep phase and there is no more propagations"() {
        given:
        userFollowing = new UserFollowing(id: USER_FOLLOWING_ID, phase: SLEEP_PHASE)
        userFollowingRepository.findOldestPropagationInDiscoverPhase() >> userFollowing
        pendingRequestRepository.countByPropagationId(USER_FOLLOWING_ID) >> 1

        when:
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowing.phase == SLEEP_PHASE
        0 * userFollowingRepository.save(*_)
    }

    def "entity is not moved to report phase when there is more propagations"() {
        given:
        userFollowing = new UserFollowing(id: USER_FOLLOWING_ID, phase: DISCOVER_PHASE)
        userFollowingRepository.findOldestPropagationInDiscoverPhase() >> userFollowing
        pendingRequestRepository.countByPropagationId(USER_FOLLOWING_ID) >> 1

        when:
        userFollowingDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowing.phase == DISCOVER_PHASE
        0 * userFollowingRepository.save(*_)
    }

}
