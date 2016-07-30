package com.cezarykluczynski.carmen.vcs.git.worker

import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO
import com.cezarykluczynski.carmen.model.github.Repository
import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.model.github.User
import com.cezarykluczynski.carmen.util.DateUtil
import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand
import com.cezarykluczynski.carmen.util.exec.command.Command
import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.util.exec.result.Result
import com.cezarykluczynski.carmen.util.filesystem.Directory
import com.cezarykluczynski.carmen.vcs.server.Server
import com.cezarykluczynski.carmen.vcs.server.ServerTest
import spock.lang.Shared
import spock.lang.Specification

class GitHubCloneWorkerTest extends Specification {

    private static final String LOCATION_DIRECTORY = 'c/c2'
    private static final String LOCATION_SUBDIRECTORY = 'test/test'
    private static final String CLONE_URL = '.'
    private static final String RESULTING_CLONE_DIRECTORY = 'target/test-storage/c/c2/test/test'

    private RepositoriesClonesDAO repositoriesClonesDAOMock

    private GitHubCloneWorker gitHubCloneWorker

    private RepositoriesDAO repositoriesDAOMock

    private Server serverMock

    private User userEntity

    private Repository repositoryEntity

    @Shared
    private Date now

    void setupSpec() {
        now = DateUtil.now()
    }

    def setup() {
        userEntity = Mock User
        repositoryEntity = Mock(Repository) {
            getUser() >> userEntity
            getCloneUrl() >> CLONE_URL
            getFullName() >> LOCATION_SUBDIRECTORY
        }

        repositoriesClonesDAOMock = Mock RepositoriesClonesDAO
        repositoriesDAOMock = Mock RepositoriesDAO
        serverMock = Mock(Server) {
            getCloneRoot() >> ServerTest.CLONE_ROOT
        }

        gitHubCloneWorker = new GitHubCloneWorker(repositoriesDAOMock, repositoriesClonesDAOMock, serverMock)
    }

    def "local repository can be cloned"() {
        given:
        repositoriesDAOMock.findNotForkedRepositoryWithoutClone() >> repositoryEntity
        RepositoryClone repositoryCloneEntity = Mock(RepositoryClone) {
            getLocationDirectory() >> LOCATION_DIRECTORY
            getLocationSubdirectory() >> LOCATION_SUBDIRECTORY
        }
        repositoriesClonesDAOMock.createStubEntity(*_) >> repositoryCloneEntity

        when:
        gitHubCloneWorker.run()

        String cloneDirectory = "${ServerTest.CLONE_ROOT}/${LOCATION_DIRECTORY}/${LOCATION_SUBDIRECTORY}"
        String revParseCommandBody = "git rev-parse --resolve-git-dir ${cloneDirectory}/.git"
        Command revParseCommand = new ApacheCommonsCommand(revParseCommandBody)
        Result revParseCommandResult = Executor.execute(revParseCommand)

        then:
        1 * repositoriesClonesDAOMock.setStatusToCloned(repositoryCloneEntity)
        revParseCommandResult.isSuccessFul()
        revParseCommandResult.getOutput().contains(repositoryEntity.getFullName())

        cleanup:
        Directory.delete RESULTING_CLONE_DIRECTORY
    }

    def "null repository entity does not generate clone repository entity"() {
        given:
        repositoriesDAOMock.findNotForkedRepositoryWithoutClone() >> null

        when:
        gitHubCloneWorker.run()

        then:
        0 * repositoriesClonesDAOMock.createStubEntity(*_) >> null
    }

    def "invalid repository cannot be cloned"() {
        given:
        repositoriesDAOMock.findNotForkedRepositoryWithoutClone() >> repositoryEntity
        RepositoryClone repositoryCloneEntity = Mock(RepositoryClone) {
            getLocationDirectory() >> '.'
            getLocationSubdirectory() >> '.'
        }
        repositoriesClonesDAOMock.createStubEntity(*_) >> repositoryCloneEntity
        repositoryEntity.getCloneUrl() >> "target"

        when:
        gitHubCloneWorker.run()

        then:
        1 * repositoriesClonesDAOMock.truncateEntity(*_)

        cleanup:
        Directory.delete RESULTING_CLONE_DIRECTORY
    }

    def "invalid clone repository does not make clone"() {
        given:
        GitHubCloneWorker gitHubCloneWorker = new GitHubCloneWorker(repositoriesDAOMock, repositoriesClonesDAOMock, serverMock)
        repositoriesClonesDAOMock.createStubEntity(serverMock, repositoryEntity) >> null

        when:
        gitHubCloneWorker.run()

        then:
        0 * repositoriesClonesDAOMock.setStatusToCloned(_)
        0 * repositoriesClonesDAOMock.truncateEntity(*_)
    }

}
