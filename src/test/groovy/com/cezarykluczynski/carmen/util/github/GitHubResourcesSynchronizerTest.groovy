package com.cezarykluczynski.carmen.util.github

import org.testng.annotations.Test
import org.testng.annotations.BeforeMethod
import org.testng.Assert

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
import static org.mockito.Mockito.withSettings

import static org.hamcrest.Matchers.hasItems
import static org.hamcrest.MatcherAssert.assertThat

class GitHubResourcesSynchronizerTest {

    List<GitHubResource> storedResources

    List<GitHubResource> currentResources

    def resource1
    def resource2
    def resource3
    def resource4
    def resource5

    @BeforeMethod
    void setUp() {
        // setup
        resource1 = mock Object.class, withSettings().extraInterfaces(GitHubResource.class)
        when resource1.getGitHubResourceId() thenReturn 1L
        resource2 = mock Object.class, withSettings().extraInterfaces(GitHubResource.class)
        when resource2.getGitHubResourceId() thenReturn 2L
        resource3 = mock Object.class, withSettings().extraInterfaces(GitHubResource.class)
        when resource3.getGitHubResourceId() thenReturn 3L
        resource4 = mock Object.class, withSettings().extraInterfaces(GitHubResource.class)
        when resource4.getGitHubResourceId() thenReturn 4L
        resource5 = mock Object.class, withSettings().extraInterfaces(GitHubResource.class)
        when resource5.getGitHubResourceId() thenReturn 5L

        storedResources = new ArrayList<GitHubResource>()
        currentResources = new ArrayList<GitHubResource>()
    }

    @Test
    void emptyStoredListAndNonEmptyCurrentList() {
        // setup
        currentResources.add resource1
        currentResources.add resource2

        // exercise
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        // assertion
        Assert.assertEquals gitHubResourcesSynchronizer.getResourcesToDelete().size(), 0
        Assert.assertEquals gitHubResourcesSynchronizer.getResourcesToPreserve().size(), 0
        assertThat gitHubResourcesSynchronizer.getResourcesToCreate(), hasItems(resource1, resource2)
    }

    @Test
    void nonEmptyStoredListAndEmptyCurrentList() {
        // setup
        storedResources.add resource1
        storedResources.add resource2

        // exercise
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        // assertion
        assertThat gitHubResourcesSynchronizer.getResourcesToDelete(), hasItems(resource1, resource2)
        Assert.assertEquals gitHubResourcesSynchronizer.getResourcesToPreserve().size(), 0
        Assert.assertEquals gitHubResourcesSynchronizer.getResourcesToCreate().size(), 0
    }

    @Test
    void nonEmptyStoredListAndNonEmptyCurrentListDisjointed() {
        // setup
        storedResources.add resource1
        storedResources.add resource2
        currentResources.add resource3
        currentResources.add resource4

        // exercise
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        // assertion
        assertThat gitHubResourcesSynchronizer.getResourcesToDelete(), hasItems(resource1, resource2)
        Assert.assertEquals gitHubResourcesSynchronizer.getResourcesToPreserve().size(), 0
        assertThat gitHubResourcesSynchronizer.getResourcesToCreate(), hasItems(resource3, resource4)
    }

    @Test
    void nonEmptyStoredListAndNonEmptyCurrentListIntersected() {
        // setup
        storedResources.add resource1
        storedResources.add resource2
        storedResources.add resource3
        currentResources.add resource3
        currentResources.add resource4
        currentResources.add resource5

        // exercise
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        // assertion
        assertThat gitHubResourcesSynchronizer.getResourcesToDelete(), hasItems(resource1, resource2)
        assertThat gitHubResourcesSynchronizer.getResourcesToPreserve(), hasItems(resource3)
        assertThat gitHubResourcesSynchronizer.getResourcesToCreate(), hasItems(resource4, resource5)
    }

    @Test
    void identicalLists() {
        // setup
        storedResources.add resource1
        storedResources.add resource2
        currentResources.add resource1
        currentResources.add resource2

        // exercise
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        // assertion
        Assert.assertEquals gitHubResourcesSynchronizer.getResourcesToDelete().size(), 0
        assertThat gitHubResourcesSynchronizer.getResourcesToPreserve(), hasItems(resource1, resource2)
        Assert.assertEquals gitHubResourcesSynchronizer.getResourcesToCreate().size(), 0
    }

}
