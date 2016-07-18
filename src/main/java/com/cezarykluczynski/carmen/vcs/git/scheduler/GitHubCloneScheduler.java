package com.cezarykluczynski.carmen.vcs.git.scheduler;

import com.cezarykluczynski.carmen.vcs.git.worker.GitHubCloneWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GitHubCloneScheduler {

    private GitHubCloneWorker githubCloneWorker;

    private TaskExecutor taskExecutor;

    @Autowired
    public GitHubCloneScheduler(GitHubCloneWorker githubCloneWorker, TaskExecutor taskExecutor) {
        this.githubCloneWorker = githubCloneWorker;
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedDelay = 10000)
    public void execute() {
        taskExecutor.execute(githubCloneWorker);
    }

}
