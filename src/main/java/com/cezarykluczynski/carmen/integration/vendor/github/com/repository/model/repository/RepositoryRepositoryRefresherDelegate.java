package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;
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

    private List<RepositoryDTO> repositoriesDTOs;

    private List<Repository> existingRepositories;

    private List<RepositoryDTO> repositoriesToCreate;

    private List<Repository> repositoriesToDelete;

    private List<RepositoryDTO> repositoriesToPreserve;

    private GitHubResourcesSynchronizer repositoriesSynchronizer;

    // TODO remove synchronized
    public synchronized void refresh(User userEntity,
            List<RepositoryDTO> repositoriesSetList,
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
        repositoriesToCreate = (List<RepositoryDTO>)(List<?>)
                repositoriesSynchronizer.getResourcesToCreate();
        repositoriesToDelete = (List<Repository>)(List<?>) repositoriesSynchronizer.getResourcesToDelete();
        repositoriesToPreserve = (List<RepositoryDTO>)(List<?>)
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
        for (RepositoryDTO repositoryDTOToCreate : repositoriesToCreate) {
            Repository repositoryEntity = new Repository();
            repositoryEntity = hydrateEntityWithSet(repositoryEntity, repositoryDTOToCreate);
            repositoryRepository.save(repositoryEntity);
        }
    }

    private void refreshRepositoriesToPreserve() {
        for (RepositoryDTO repositoryDTOToPreserve : repositoriesToPreserve) {
            for (Repository repositoryEntity : existingRepositories) {
                if (repositoryDTOToPreserve.getId().equals(repositoryEntity.getId()) ){
                    repositoryEntity = hydrateEntityWithSet(repositoryEntity, repositoryDTOToPreserve);
                    repositoryRepository.save(repositoryEntity);
                    break;
                }
            }
        }
    }

    private Repository hydrateEntityWithSet(Repository repositoryEntity,
            RepositoryDTO repositoryDTO) {
        repositoryEntity.setUser(user);
        repositoryEntity.setGithubId(repositoryDTO.getId());

        repositoryEntity.setName(repositoryDTO.getName());
        repositoryEntity.setFullName(repositoryDTO.getFullName());
        repositoryEntity.setDescription(repositoryDTO.getDescription());
        repositoryEntity.setHomepage(repositoryDTO.getHomepage());

        repositoryEntity.setFork(repositoryDTO.isFork());
        repositoryEntity.setDefaultBranch(repositoryDTO.getDefaultBranch());
        repositoryEntity.setCloneUrl(repositoryDTO.getCloneUrl());

        repositoryEntity.setPushed(repositoryDTO.getPushed());
        repositoryEntity.setCreated(repositoryDTO.getCreated());
        repositoryEntity.setUpdated(repositoryDTO.getUpdated());

        return repositoryEntity;
    }

}
