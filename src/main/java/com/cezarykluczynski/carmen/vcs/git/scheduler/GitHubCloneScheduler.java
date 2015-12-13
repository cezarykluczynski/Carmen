package com.cezarykluczynski.carmen.vcs.git.scheduler;

import com.cezarykluczynski.carmen.vcs.git.worker.GitHubCloneWorker;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

public class GitHubCloneScheduler {

    @Autowired
    private GitHubCloneWorker githubCloneWorker;

    private TaskExecutor taskExecutor;

    public GitHubCloneScheduler(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedRate = 10000)
    public void execute() {
        taskExecutor.execute(githubCloneWorker);
    }

}
