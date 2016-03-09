package com.cezarykluczynski.carmen.cron.github.executor;

import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestFactory;
import com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO;
import com.cezarykluczynski.carmen.model.propagations.Repositories;
import com.cezarykluczynski.carmen.util.DateTimeConstants;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Component
public class RepositoriesWakeUpExecutor implements Runnable {

    private RepositoriesDAO propagationsRepositoriesDAOImpl;

    private PendingRequestFactory pendingRequestFactory;

    private Integer refreshIntervalDays;

    private Repositories repositoriesEntity;

    @Autowired
    public RepositoriesWakeUpExecutor(RepositoriesDAO propagationsRepositoriesDAOImpl,
                                      PendingRequestFactory pendingRequestFactory,
                      @Value("${executor.RepositoriesWakeUpExecutor.refreshIntervalDays}") Integer refreshIntervalDays
    ) {
        this.propagationsRepositoriesDAOImpl = propagationsRepositoriesDAOImpl;
        this.pendingRequestFactory = pendingRequestFactory;
        this.refreshIntervalDays = refreshIntervalDays;
    }

    public void run() {
        repositoriesEntity = propagationsRepositoriesDAOImpl.findOldestPropagationInSleepPhase();

        if (repositoriesEntity == null) {
            return;
        }

        tryToMoveToRefreshPhase();
    }

    private void tryToMoveToRefreshPhase() {
        if (repositoriesEntity.getUpdated().before(dateThresholdForWakeUp())) {
            propagationsRepositoriesDAOImpl.moveToRefreshPhase(repositoriesEntity);
            pendingRequestFactory.createPendingRequestForUserRepositoriesPropagation(repositoriesEntity);
        }
    }

    private Date dateThresholdForWakeUp() {
        return new Date(
                System.currentTimeMillis() - (refreshIntervalDays * DateTimeConstants.MILLISECONDS_IN_DAY.getValue()));
    }

}
