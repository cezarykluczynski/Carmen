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

    @Value("${executor.RepositoriesWakeUpExecutor.refreshIntervalDays}")
    private Integer refreshIntervalDays;

    @Autowired
    RepositoriesDAO propagationsRepositoriesDAOImpl;

    @Autowired
    PendingRequestFactory pendingRequestFactory;

    Repositories repositoriesEntity;

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
