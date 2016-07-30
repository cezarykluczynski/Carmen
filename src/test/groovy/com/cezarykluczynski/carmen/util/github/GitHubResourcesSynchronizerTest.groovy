package com.cezarykluczynski.carmen.util.github

import com.beust.jcommander.internal.Lists
import spock.lang.Shared
import spock.lang.Specification

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class GitHubResourcesSynchronizerTest extends Specification {

    private List<GitHubResource> storedResources

    private List<GitHubResource> currentResources

    @Shared
    private GitHubResource resource1
    @Shared
    private GitHubResource resource2
    @Shared
    private GitHubResource resource3
    @Shared
    private GitHubResource resource4
    @Shared
    private GitHubResource resource5

    def setupSpec() {
        resource1 = mock GitHubResource
        when resource1.getGitHubResourceId() thenReturn 1L
        resource2 = mock GitHubResource
        when resource2.getGitHubResourceId() thenReturn 2L
        resource3 = mock GitHubResource
        when resource3.getGitHubResourceId() thenReturn 3L
        resource4 = mock GitHubResource
        when resource4.getGitHubResourceId() thenReturn 4L
        resource5 = mock GitHubResource
        when resource5.getGitHubResourceId() thenReturn 5L
    }

    def setup() {
        storedResources = Lists.newArrayList()
        currentResources = Lists.newArrayList()
    }

    def emptyStoredListAndNonEmptyCurrentList() {
        given:
        currentResources.add resource1
        currentResources.add resource2

        when:
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        then:
        gitHubResourcesSynchronizer.getResourcesToDelete().empty
        gitHubResourcesSynchronizer.getResourcesToPreserve().empty
        gitHubResourcesSynchronizer.getResourcesToCreate().containsAll(Lists.newArrayList(resource1, resource2))
    }

    def nonEmptyStoredListAndEmptyCurrentList() {
        given:
        storedResources.add resource1
        storedResources.add resource2

        when:
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        then:
        gitHubResourcesSynchronizer.getResourcesToDelete().containsAll(Lists.newArrayList(resource1, resource2))
        gitHubResourcesSynchronizer.getResourcesToPreserve().empty
        gitHubResourcesSynchronizer.getResourcesToCreate().empty
    }

    def nonEmptyStoredListAndNonEmptyCurrentListDisjointed() {
        given:
        storedResources.add resource1
        storedResources.add resource2
        currentResources.add resource3
        currentResources.add resource4

        when:
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        then:
        gitHubResourcesSynchronizer.getResourcesToDelete().containsAll(Lists.newArrayList(resource1, resource2))
        gitHubResourcesSynchronizer.getResourcesToPreserve().empty
        gitHubResourcesSynchronizer.getResourcesToCreate().containsAll(Lists.newArrayList(resource3, resource4))
    }

    def nonEmptyStoredListAndNonEmptyCurrentListIntersected() {
        given:
        storedResources.add resource1
        storedResources.add resource2
        storedResources.add resource3
        currentResources.add resource3
        currentResources.add resource4
        currentResources.add resource5

        when:
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        then:
        gitHubResourcesSynchronizer.getResourcesToDelete().containsAll(Lists.newArrayList(resource1, resource2))
        gitHubResourcesSynchronizer.getResourcesToPreserve().containsAll(Lists.newArrayList(resource3))
        gitHubResourcesSynchronizer.getResourcesToCreate().containsAll(Lists.newArrayList(resource4, resource5))
    }

    def identicalLists() {
        given:
        storedResources.add resource1
        storedResources.add resource2
        currentResources.add resource1
        currentResources.add resource2

        when:
        GitHubResourcesSynchronizer gitHubResourcesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources, currentResources
        )

        then:
        gitHubResourcesSynchronizer.getResourcesToDelete().empty
        gitHubResourcesSynchronizer.getResourcesToPreserve().containsAll(Lists.newArrayList(resource1, resource2))
        gitHubResourcesSynchronizer.getResourcesToCreate().empty
    }

}
