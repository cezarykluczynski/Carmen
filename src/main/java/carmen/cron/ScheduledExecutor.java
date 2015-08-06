package carmen.cron;

import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

public class ScheduledExecutor {

    @Autowired
    PropagationExecutor propagationExecutor;

    private class ScheduledPropagationExecutor implements Runnable {

        private PropagationExecutor propagationExecutor;

        public ScheduledPropagationExecutor(PropagationExecutor propagationExecutor) {
            this.propagationExecutor = propagationExecutor;
        }

        public void run() {
            propagationExecutor.setup();
            propagationExecutor.run();
            propagationExecutor.teardown();
        }
    }

    private TaskExecutor taskExecutor;

    public ScheduledExecutor(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 1000)
    public void executePropagation() {
        taskExecutor.execute(new ScheduledPropagationExecutor(propagationExecutor));
    }

}