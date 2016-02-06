package com.cezarykluczynski.carmen.util.github;

import java.util.List;
import java.util.ArrayList;

public class GitHubResourcesSynchronizer {

    List<GitHubResource> storedResources;

    List<GitHubResource> currentResources;

    List<GitHubResource> resourcesToDelete = new ArrayList<>();

    List<GitHubResource> resourcesToCreate = new ArrayList<>();

    List<GitHubResource> resourcesToPreserve = new ArrayList<>();

    public GitHubResourcesSynchronizer(List<GitHubResource> storedResources, List<GitHubResource> currentResources) {
        this.storedResources = storedResources;
        this.currentResources = currentResources;
        findResourcesToDelete();
        findResourcesToCreateAndPreserve();
    }

    public List<GitHubResource> getResourcesToDelete() {
        return resourcesToDelete;
    }

    public List<GitHubResource> getResourcesToCreate() {
        return resourcesToCreate;
    }

    public List<GitHubResource> getResourcesToPreserve() {
        return resourcesToPreserve;
    }

    private void findResourcesToDelete() {
        storedResources.stream()
            .filter(storedResource -> !listHasItem(currentResources, storedResource))
            .forEach(resourcesToDelete::add);
    }

    private void findResourcesToCreateAndPreserve() {
        for (GitHubResource currentResource : currentResources) {
            if (!listHasItem(storedResources, currentResource)) {
                resourcesToCreate.add(currentResource);
            } else {
                resourcesToPreserve.add(currentResource);
            }
        }
    }

    private boolean listHasItem(List<GitHubResource> githubResourcesList, GitHubResource gitHubResourceToFind) {
        GitHubResource githubResourceFound = findCorrespondingResourceInList(gitHubResourceToFind, githubResourcesList);
        return githubResourceFound != null;
    }

    private GitHubResource findCorrespondingResourceInList(
        GitHubResource gitHubResourceToCorrespondTo, List<GitHubResource> githubResourcesList
    ) {
        for (GitHubResource gitHubResourceToCompare : githubResourcesList) {
            if (gitHubResourceToCorrespondTo.getGitHubResourceId().equals(gitHubResourceToCompare.getGitHubResourceId())) {
                return gitHubResourceToCompare;
            }
        }

        return null;
    }

}
