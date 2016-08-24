package com.cezarykluczynski.carmen.util.github

import com.google.common.collect.Lists
import spock.lang.Shared
import spock.lang.Specification

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
        resource1 = Mock GitHubResource
        resource1.getGitHubResourceId() >> 1L
        resource2 = Mock GitHubResource
        resource2.getGitHubResourceId() >> 2L
        resource3 = Mock GitHubResource
        resource3.getGitHubResourceId() >> 3L
        resource4 = Mock GitHubResource
        resource4.getGitHubResourceId() >> 4L
        resource5 = Mock GitHubResource
        resource5.getGitHubResourceId() >> 5L
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
