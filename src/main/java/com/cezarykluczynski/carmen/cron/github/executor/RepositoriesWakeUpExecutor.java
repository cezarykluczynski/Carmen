package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.cron.management.annotations.DatabaseSwitchableJob;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.entity.Repositories;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepository;
import com.cezarykluczynski.carmen.util.DateTimeConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@DatabaseSwitchableJob
public class RepositoriesWakeUpExecutor implements Runnable {

    private RepositoriesRepository repositoriesRepository;

    private PendingRequestFactory pendingRequestFactory;

    private Integer refreshIntervalDays;

    @Autowired
    public RepositoriesWakeUpExecutor(RepositoriesRepository repositoriesRepository,
                                      PendingRequestFactory pendingRequestFactory,
                      @Value("${executor.RepositoriesWakeUpExecutor.refreshIntervalDays}") Integer refreshIntervalDays
    ) {
        this.repositoriesRepository = repositoriesRepository;
        this.pendingRequestFactory = pendingRequestFactory;
        this.refreshIntervalDays = refreshIntervalDays;
    }

    public void run() {
        Repositories repositoriesEntity = repositoriesRepository.findOldestPropagationInSleepPhase();

        if (repositoriesEntity != null) {
            tryToMoveToRefreshPhase(repositoriesEntity);
        }
    }

    private void tryToMoveToRefreshPhase(Repositories repositoriesEntity) {
        if (repositoriesEntity.getUpdated().before(dateThresholdForWakeUp())) {
            repositoriesRepository.moveToRefreshPhase(repositoriesEntity);
            pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity);
        }
    }

    private Date dateThresholdForWakeUp() {
        return new Date(
                System.currentTimeMillis() - (refreshIntervalDays * DateTimeConstants.MILLISECONDS_IN_DAY.getValue()));
    }

}
