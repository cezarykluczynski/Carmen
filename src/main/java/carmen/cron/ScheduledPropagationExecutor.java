package carmen.cron;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledPropagationExecutor {

    @Autowired
    PropagationExecutor propagationExecutor;

    private class ScheduledPropagationExecutorRunnable implements Runnable {

        private PropagationExecutor propagationExecutor;

        public ScheduledPropagationExecutorRunnable(PropagationExecutor propagationExecutor) {
            this.propagationExecutor = propagationExecutor;
        }

        public void run() {
            propagationExecutor.run();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledPropagationExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 1000)
    public void executePropagation() {
        taskExecutor.execute(new ScheduledPropagationExecutorRunnable(propagationExecutor));
    }

}