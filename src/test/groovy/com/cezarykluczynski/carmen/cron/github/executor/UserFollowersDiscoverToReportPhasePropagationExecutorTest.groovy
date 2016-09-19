package com.cezarykluczynski.carmen.cron.github.executor

import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.UserFollowers
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.UserFollowersRepository
import spock.lang.Specification

class UserFollowersDiscoverToReportPhasePropagationExecutorTest extends Specification {

    private static final Integer USER_FOLLOWERS_ID = 1
    private static final String DISCOVER_PHASE = "discover"
    private static final String REPORT_PHASE = "report"
    private static final String SLEEP_PHASE = "sleep"

    private UserFollowersRepository userFollowersRepository

    private PendingRequestRepository pendingRequestRepository

    private UserFollowersDiscoverToReportPhasePropagationExecutor userFollowersDiscoverToReportPhasePropagationExecutor

    private UserFollowers userFollowers

    def setup() {
        userFollowersRepository = Mock UserFollowersRepository
        pendingRequestRepository = Mock PendingRequestRepository
        userFollowersDiscoverToReportPhasePropagationExecutor = new UserFollowersDiscoverToReportPhasePropagationExecutor(
                userFollowersRepository, pendingRequestRepository)
    }

    def "entity is moved to report phase when there is no more propagations"() {
        given:
        userFollowers = new UserFollowers(id: USER_FOLLOWERS_ID, phase: DISCOVER_PHASE)
        userFollowersRepository.findOldestPropagationInDiscoverPhase() >> userFollowers
        pendingRequestRepository.countByPropagationId(USER_FOLLOWERS_ID) >> 0

        when:
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        then:
        1 * userFollowersRepository.save(_ as UserFollowers) >> { UserFollowers userFollowersArg ->
            assert userFollowersArg == userFollowers
            assert userFollowersArg.getPhase() == REPORT_PHASE
        }
    }

    def "does not move to report phase when entity is in sleep phase and there is no more propagations"() {
        given:
        userFollowers = new UserFollowers(id: USER_FOLLOWERS_ID, phase: SLEEP_PHASE)
        userFollowersRepository.findOldestPropagationInDiscoverPhase() >> userFollowers
        pendingRequestRepository.countByPropagationId(USER_FOLLOWERS_ID) >> 1

        when:
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowers.phase == SLEEP_PHASE
        0 * userFollowersRepository.save(*_)
    }

    def "entity is not moved to report phase when there is more propagations"() {
        given:
        userFollowers = new UserFollowers(id: USER_FOLLOWERS_ID, phase: DISCOVER_PHASE)
        userFollowersRepository.findOldestPropagationInDiscoverPhase() >> userFollowers
        pendingRequestRepository.countByPropagationId(USER_FOLLOWERS_ID) >> 1

        when:
        userFollowersDiscoverToReportPhasePropagationExecutor.run()

        then:
        userFollowers.phase == DISCOVER_PHASE
        0 * userFollowersRepository.save(*_)
    }

}
