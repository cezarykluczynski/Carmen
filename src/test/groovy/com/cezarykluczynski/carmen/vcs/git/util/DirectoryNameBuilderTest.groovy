package com.cezarykluczynski.carmen.vcs.git.util

import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.vcs.server.Server
import spock.lang.Specification

class DirectoryNameBuilderTest extends Specification {

    private static final String CLONE_ROOT = "a"
    private static final String LOCATION_DIRECTORY = "b"
    private static final String LOCATION_SUBDIRECTORY = "c"

    private Server serverMock

    private RepositoryClone repositoryCloneMock

    def setup() {
        serverMock = Mock Server
        repositoryCloneMock = Mock RepositoryClone

        serverMock.getCloneRoot() >> CLONE_ROOT
        repositoryCloneMock.getLocationDirectory() >> LOCATION_DIRECTORY
        repositoryCloneMock.getLocationSubdirectory() >> LOCATION_SUBDIRECTORY
    }

    def "builds clone directory"() {
        when:
        String cloneDirectory = DirectoryNameBuilder.buildCloneDirectory serverMock, repositoryCloneMock

        then:
        cloneDirectory == CLONE_ROOT + "/" + LOCATION_DIRECTORY + "/" + LOCATION_SUBDIRECTORY
    }

}
