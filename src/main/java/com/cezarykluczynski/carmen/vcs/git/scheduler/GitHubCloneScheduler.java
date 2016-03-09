package com.cezarykluczynski.carmen.vcs.git.scheduler;

import com.cezarykluczynski.carmen.vcs.git.worker.GitHubCloneWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GitHubCloneScheduler {

    private GitHubCloneWorker githubCloneWorker;

    @Autowired
    public GitHubCloneScheduler(GitHubCloneWorker githubCloneWorker) {
        this.githubCloneWorker = githubCloneWorker;
    }


    private TaskExecutor taskExecutor;

    public GitHubCloneScheduler(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    @Scheduled(fixedRate = 10000)
    public void execute() {
        taskExecutor.execute(githubCloneWorker);
    }

}
