package com.cezarykluczynski.carmen.util.github;

import java.util.List;
import java.util.ArrayList;

public class GitHubResourcesSynchronizer {

    List<GitHubResource> storedResources;

    List<GitHubResource> currentResources;

    List<GitHubResource> resourcesToDelete = new ArrayList<GitHubResource>();

    List<GitHubResource> resourcesToCreate = new ArrayList<GitHubResource>();

    List<GitHubResource> resourcesToPreserve = new ArrayList<GitHubResource>();

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
        for (GitHubResource storedResource : storedResources) {
            if (!listHasItem(currentResources, storedResource)) {
                resourcesToDelete.add(storedResource);
            }
        }
    }

    private void findResourcesToCreateAndPreserve() {
        for (GitHubResource currentResource : currentResources) {
            if (!listHasItem(storedResources, currentResource)) {
                resourcesToCreate.add(currentResource);
            } else {
                resourcesToPreserve.add(findCorrespondingResourceInList(currentResource, storedResources));
            }
        }
    }

    private boolean listHasItem(List<GitHubResource> githubResourcesList, GitHubResource gitHubResourceToFind) {
        GitHubResource githubResourceFound = findCorrespondingResourceInList(gitHubResourceToFind, githubResourcesList);
        return githubResourceFound instanceof GitHubResource;
    }

    private GitHubResource findCorrespondingResourceInList(
        GitHubResource gitHubResourceToCorrespondTo, List<GitHubResource> githubResourcesList
    ) {
        for (GitHubResource gitHubResourceToCompare : githubResourcesList) {
            if (gitHubResourceToCorrespondTo.getGitHubResourceId() == gitHubResourceToCompare.getGitHubResourceId()) {
                return gitHubResourceToCompare;
            }
        }

        return null;
    }

}
