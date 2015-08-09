package carmen.cron;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledAPIQueryExecutor {

    @Autowired
    APIQueryExecutor apiQueryExecutor;

    private class ScheduledAPIQueryExecutorRunnable implements Runnable {

        private APIQueryExecutor apiQueryExecutor;

        public ScheduledAPIQueryExecutorRunnable(APIQueryExecutor apiQueryExecutor) {
            this.apiQueryExecutor = apiQueryExecutor;
        }

        public void run() {
            apiQueryExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledAPIQueryExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 1000)
    public void executePropagation() {
        taskExecutor.execute(new ScheduledAPIQueryExecutorRunnable(apiQueryExecutor));
    }

}