package com.cezarykluczynski.carmen.vcs.git.worker

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.RepositoryCloneRepository
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.RepositoryRepository
import com.cezarykluczynski.carmen.util.DateUtil
import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand
import com.cezarykluczynski.carmen.util.exec.command.Command
import com.cezarykluczynski.carmen.util.exec.executor.Executor
import com.cezarykluczynski.carmen.util.exec.result.Result
import com.cezarykluczynski.carmen.vcs.server.Server
import org.apache.commons.io.FileUtils
import spock.lang.Shared
import spock.lang.Specification

class GitHubCloneWorkerTest extends Specification {

    private static final String CLONE_ROOT = "tmp/test-storage/" + System.currentTimeMillis() + "/"
    private static final String LOCATION_DIRECTORY = 'c/c2'
    private static final String LOCATION_SUBDIRECTORY = 'test/test'
    private static final String CLONE_URL = '.'
    private static final String RESULTING_CLONE_DIRECTORY = "${CLONE_ROOT}/${LOCATION_DIRECTORY}/${LOCATION_SUBDIRECTORY}"

    private RepositoryCloneRepository repositoryCloneRepository

    private GitHubCloneWorker gitHubCloneWorker

    private RepositoryRepository repositoryRepository

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

        repositoryCloneRepository = Mock RepositoryCloneRepository
        repositoryRepository = Mock RepositoryRepository
        serverMock = Mock(Server) {
            getCloneRoot() >> CLONE_ROOT
        }

        gitHubCloneWorker = new GitHubCloneWorker(repositoryRepository, repositoryCloneRepository, serverMock)
    }

    def "local repository can be cloned"() {
        given:
        repositoryRepository.findFirstByRepositoryCloneIsNullAndForkFalse() >> repositoryEntity
        RepositoryClone repositoryCloneEntity = Mock(RepositoryClone) {
            getLocationDirectory() >> LOCATION_DIRECTORY
            getLocationSubdirectory() >> LOCATION_SUBDIRECTORY
        }
        repositoryCloneRepository.createStubEntity(*_) >> repositoryCloneEntity

        when:
        gitHubCloneWorker.run()

        String cloneDirectory = "${CLONE_ROOT}/${LOCATION_DIRECTORY}/${LOCATION_SUBDIRECTORY}"
        String revParseCommandBody = "git rev-parse --resolve-git-dir ${cloneDirectory}/.git"
        Command revParseCommand = new ApacheCommonsCommand(revParseCommandBody)
        Result revParseCommandResult = Executor.execute(revParseCommand)

        then:
        1 * repositoryCloneRepository.setStatusToCloned(repositoryCloneEntity)
        revParseCommandResult.isSuccessFul()
        revParseCommandResult.getOutput().contains(repositoryEntity.getFullName())

        cleanup:
        FileUtils.deleteQuietly new File(RESULTING_CLONE_DIRECTORY)
    }

    def "null repository entity does not generate clone repository entity"() {
        given:
        repositoryRepository.findFirstByRepositoryCloneIsNullAndForkFalse() >> null

        when:
        gitHubCloneWorker.run()

        then:
        0 * repositoryCloneRepository.createStubEntity(*_)
    }

    def "invalid repository cannot be cloned"() {
        given:
        repositoryRepository.findFirstByRepositoryCloneIsNullAndForkFalse() >> repositoryEntity
        RepositoryClone repositoryCloneEntity = Mock(RepositoryClone) {
            getLocationDirectory() >> '.'
            getLocationSubdirectory() >> '.'
        }
        repositoryCloneRepository.createStubEntity(*_) >> repositoryCloneEntity
        repositoryEntity.getCloneUrl() >> "target"

        when:
        gitHubCloneWorker.run()

        then:
        1 * repositoryCloneRepository.truncateEntity(*_)

        cleanup:
        FileUtils.deleteQuietly new File(RESULTING_CLONE_DIRECTORY)
    }

    def "invalid clone repository does not make clone"() {
        given:
        GitHubCloneWorker gitHubCloneWorker = new GitHubCloneWorker(repositoryRepository, repositoryCloneRepository, serverMock)
        repositoryCloneRepository.createStubEntity(serverMock, repositoryEntity) >> null

        when:
        gitHubCloneWorker.run()

        then:
        0 * repositoryCloneRepository.setStatusToCloned(_)
        0 * repositoryCloneRepository.truncateEntity(*_)
    }

}
