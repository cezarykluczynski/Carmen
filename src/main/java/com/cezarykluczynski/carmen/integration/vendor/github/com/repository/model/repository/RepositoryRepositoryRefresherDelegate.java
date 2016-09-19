package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.util.github.GitHubResource;
import com.cezarykluczynski.carmen.util.github.GitHubResourcesSynchronizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepositoryRepositoryRefresherDelegate {

    @Autowired
    private RepositoryRepository repositoryRepository;

    private User user;

    private List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesDTOs;

    private List<Repository> existingRepositories;

    private List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesToCreate;

    private List<Repository> repositoriesToDelete;

    private List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesToPreserve;

    private GitHubResourcesSynchronizer repositoriesSynchronizer;

    // TODO remove synchronized
    public synchronized void refresh(User userEntity,
            List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesSetList,
            List<Repository> repositoriesListExisting) {
        this.user = userEntity;
        this.repositoriesDTOs = repositoriesSetList;
        this.existingRepositories = repositoriesListExisting;

        createGitHubRepositoriesSynchronizer();
        extractGitHubRepositoriesSynchronizerLists();
        refreshRepositoriesList();
    }

    private void createGitHubRepositoriesSynchronizer() {
        List<GitHubResource> currentResources = (List<GitHubResource>)(List<?>) repositoriesDTOs;
        List<GitHubResource> storedResources = (List<GitHubResource>)(List<?>) existingRepositories;

        repositoriesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources,
            currentResources
        );
    }

    private void extractGitHubRepositoriesSynchronizerLists() {
        repositoriesToCreate = (List<com.cezarykluczynski.carmen.set.github.Repository>)(List<?>)
                repositoriesSynchronizer.getResourcesToCreate();
        repositoriesToDelete = (List<Repository>)(List<?>) repositoriesSynchronizer.getResourcesToDelete();
        repositoriesToPreserve = (List<com.cezarykluczynski.carmen.set.github.Repository>)(List<?>)
                repositoriesSynchronizer.getResourcesToPreserve();
    }

    private void refreshRepositoriesList() {
        deleteRepositoriesToDelete();
        createRepositoriesToCreate();
        refreshRepositoriesToPreserve();
    }

    private void deleteRepositoriesToDelete() {
        for (Repository repositoryToDelete : repositoriesToDelete) {
            repositoryRepository.delete(repositoryToDelete);
        }
    }

    private void createRepositoriesToCreate() {
        for (com.cezarykluczynski.carmen.set.github.Repository repositoryToCreate : repositoriesToCreate) {
            Repository repositoryEntity = new Repository();
            repositoryEntity = hydrateEntityWithSet(repositoryEntity, repositoryToCreate);
            repositoryRepository.save(repositoryEntity);
        }
    }

    private void refreshRepositoriesToPreserve() {
        for (com.cezarykluczynski.carmen.set.github.Repository repositoryToPreserve : repositoriesToPreserve) {
            for (Repository repositoryEntity : existingRepositories) {
                if (repositoryToPreserve.getId().equals(repositoryEntity.getId()) ){
                    repositoryEntity = hydrateEntityWithSet(repositoryEntity, repositoryToPreserve);
                    repositoryRepository.save(repositoryEntity);
                    break;
                }
            }
        }
    }

    private Repository hydrateEntityWithSet(Repository repositoryEntity,
            com.cezarykluczynski.carmen.set.github.Repository repositorySet) {
        repositoryEntity.setUser(user);
        repositoryEntity.setGithubId(repositorySet.getId());

        repositoryEntity.setName(repositorySet.getName());
        repositoryEntity.setFullName(repositorySet.getFullName());
        repositoryEntity.setDescription(repositorySet.getDescription());
        repositoryEntity.setHomepage(repositorySet.getHomepage());

        repositoryEntity.setFork(repositorySet.isFork());
        repositoryEntity.setDefaultBranch(repositorySet.getDefaultBranch());
        repositoryEntity.setCloneUrl(repositorySet.getCloneUrl());

        repositoryEntity.setPushed(repositorySet.getPushed());
        repositoryEntity.setCreated(repositorySet.getCreated());
        repositoryEntity.setUpdated(repositorySet.getUpdated());

        return repositoryEntity;
    }

}
