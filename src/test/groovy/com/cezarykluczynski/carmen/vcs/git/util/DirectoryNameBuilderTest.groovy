package com.cezarykluczynski.carmen.vcs.git.util

import com.cezarykluczynski.carmen.model.github.RepositoryClone
import com.cezarykluczynski.carmen.vcs.server.Server
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class DirectoryNameBuilderTest {

    private static final String CLONE_ROOT = "a"
    private static final String LOCATION_DIRECTORY = "b"
    private static final String LOCATION_SUBDIRECTORY = "c"

    Server server

    RepositoryClone repositoryClone

    @BeforeMethod
    void setup() {
        server = mock Server
        repositoryClone = mock RepositoryClone

        when server.getCloneRoot() thenReturn CLONE_ROOT
        when repositoryClone.getLocationDirectory() thenReturn LOCATION_DIRECTORY
        when repositoryClone.getLocationSubdirectory() thenReturn LOCATION_SUBDIRECTORY

    }

    @Test
    void buildCloneDirectory() {
        String cloneDirectory = DirectoryNameBuilder.buildCloneDirectory(server, repositoryClone)
        Assert.assertEquals cloneDirectory, CLONE_ROOT + "/" + LOCATION_DIRECTORY + "/" + LOCATION_SUBDIRECTORY
    }

}
