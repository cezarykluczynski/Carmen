package com.cezarykluczynski.carmen.vcs.git

import com.cezarykluczynski.carmen.vcs.git.scheduler.GitHubCloneScheduler
import com.cezarykluczynski.carmen.vcs.git.worker.GitHubCloneWorker
import org.springframework.core.task.TaskExecutor
import spock.lang.Specification

class GitHubCloneSchedulerTest extends Specification {

    private GitHubCloneWorker githubCloneWorkerMock

    private TaskExecutor taskExecutorMock

    private GitHubCloneScheduler gitHubCloneScheduler

    def setup() {
        githubCloneWorkerMock = Mock GitHubCloneWorker
        taskExecutorMock = Mock TaskExecutor
        gitHubCloneScheduler = new GitHubCloneScheduler(githubCloneWorkerMock, taskExecutorMock)
    }

    def "runs runnable using executor"() {
        when:
        gitHubCloneScheduler.execute()

        then:
        1 * taskExecutorMock.execute(githubCloneWorkerMock)
    }

}


