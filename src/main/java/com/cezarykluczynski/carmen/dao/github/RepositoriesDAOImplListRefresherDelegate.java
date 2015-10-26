package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.User;

import com.cezarykluczynski.carmen.util.github.GitHubResource;
import com.cezarykluczynski.carmen.util.github.GitHubResourcesSynchronizer;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.List;

class RepositoriesDAOImplListRefresherDelegate {

    SessionFactory sessionFactory;

    Session session;

    User userEntity;

    List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesSetList;

    List<Repository> repositoriesListExisting;

    List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesToCreate;

    List<Repository> repositoriesToDelete;

    List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesToPreserve;

    GitHubResourcesSynchronizer repositoriesSynchronizer;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public RepositoriesDAOImplListRefresherDelegate(
            User userEntity,
            List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesSetList,
            List<Repository> repositoriesListExisting
    ) {
        this.userEntity = userEntity;
        this.repositoriesSetList = repositoriesSetList;
        this.repositoriesListExisting = repositoriesListExisting;
    }

    public void refresh() {
        createGitHubRepositoriesSynchronizer();
        extractGitHubRepositoriesSynchronizerLists();
        refreshRepositoriesList();
    }

    private void createGitHubRepositoriesSynchronizer() {
        List<GitHubResource> currentResources = (List<GitHubResource>)(List<?>) repositoriesSetList;
        List<GitHubResource> storedResources = (List<GitHubResource>)(List<?>) repositoriesListExisting;

        repositoriesSynchronizer = new GitHubResourcesSynchronizer(
            storedResources,
            currentResources
        );
    }

    private void extractGitHubRepositoriesSynchronizerLists() {
        repositoriesToCreate =
            (List<com.cezarykluczynski.carmen.set.github.Repository>)(List<?>) repositoriesSynchronizer.getResourcesToCreate();
        repositoriesToDelete =
            (List<Repository>)(List<?>) repositoriesSynchronizer.getResourcesToDelete();
        repositoriesToPreserve =
            (List<com.cezarykluczynski.carmen.set.github.Repository>)(List<?>) repositoriesSynchronizer.getResourcesToPreserve();
    }

    private void refreshRepositoriesList() {
        session = sessionFactory.openSession();
        deleteRepositoriesToDelete();
        createRepositoriesToCreate();
        refreshRepositoriesToPreserve();
        session.flush();
        session.close();
    }

    private void deleteRepositoriesToDelete() {
        for (Repository repositoryToDelete : repositoriesToDelete) {
            session.delete(repositoryToDelete);
        }
    }

    private void createRepositoriesToCreate() {
        for (com.cezarykluczynski.carmen.set.github.Repository repositoryToCreate : repositoriesToCreate) {
            Repository repositoryEntity = new Repository();
            repositoryEntity = hydrateEntityWithSet(repositoryEntity, repositoryToCreate);
            session.save(repositoryEntity);
        }
    }

    private void refreshRepositoriesToPreserve() {
        for (com.cezarykluczynski.carmen.set.github.Repository repositoryToPreserve : repositoriesToPreserve) {
            for (Repository repositoryEntity : repositoriesListExisting) {
                if (repositoryToPreserve.getId() == repositoryEntity.getId()) {
                    repositoryEntity = hydrateEntityWithSet(repositoryEntity, repositoryToPreserve);
                    session.update(repositoryEntity);
                    break;
                }
            }
        }
    }

    private Repository hydrateEntityWithSet(
        Repository repositoryEntity,
        com.cezarykluczynski.carmen.set.github.Repository repositorySet
    ) {
        repositoryEntity.setUser(userEntity);
        repositoryEntity.setGithubId(repositorySet.getId());

        repositoryEntity.setName(repositorySet.getName());
        repositoryEntity.setFullName(repositorySet.getFullName());
        repositoryEntity.setDescription(repositorySet.getDescription());
        repositoryEntity.setHomepage(repositorySet.getHomepage());

        repositoryEntity.setFork(repositorySet.getFork());
        repositoryEntity.setDefaultBranch(repositorySet.getDefaultBranch());
        repositoryEntity.setCloneUrl(repositorySet.getCloneUrl());

        repositoryEntity.setPushed(repositorySet.getPushed());
        repositoryEntity.setCreated(repositorySet.getCreated());
        repositoryEntity.setUpdated(repositorySet.getUpdated());

        return repositoryEntity;
    }

}
