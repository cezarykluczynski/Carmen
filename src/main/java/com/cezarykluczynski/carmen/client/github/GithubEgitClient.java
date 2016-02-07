package com.cezarykluczynski.carmen.client.github;

import java.io.IOException;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class GithubEgitClient implements GithubClientInterface {

    public GithubEgitClient(GitHubClient github) {
        this.github = github;
    }

    private GitHubClient github;

    public RateLimit getCoreLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public RateLimit getSearchLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public User getUser(String name) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public List<Repository> getRepositories(String login) throws IOException {
        RepositoryService repositoryService = new RepositoryService(github);
        List<org.eclipse.egit.github.core.Repository> repositoriesList = repositoryService.getRepositories(login);
        return mapToNativeRepositoriesPOJOList(repositoriesList);
    }

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        UserService userService = new UserService(github);
        PaginationAwareArrayList<User> userList = pageIteratorToList(userService.pageFollowers(name, offset, limit));
        userList.addPaginationLimitAndOffset(limit, offset);
        return userList;
    }

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        UserService userService = new UserService(github);
        PaginationAwareArrayList<User> userList = pageIteratorToList(userService.pageFollowing(name, offset, limit));
        userList.addPaginationLimitAndOffset(limit, offset);
        return userList;
    }

    private List<Repository> mapToNativeRepositoriesPOJOList(
            List<org.eclipse.egit.github.core.Repository> repositoriesListRemote) {
        List<Repository> repositoryListLocal = new ArrayList<Repository>();

        for (org.eclipse.egit.github.core.Repository repository : repositoriesListRemote) {
            repositoryListLocal.add(new Repository(
                repository.getId(),
                repository.getParent() == null ? null : repository.getParent().getId(),
                repository.getName(),
                repository.generateId(),
                repository.getDescription(),
                repository.getHomepage(),
                repository.isFork(),
                repository.getMasterBranch(),
                repository.getCloneUrl(),
                repository.getCreatedAt(),
                repository.getPushedAt(),
                repository.getUpdatedAt()
            ));
        }

        return repositoryListLocal;
    }

    private PaginationAwareArrayList<User> pageIteratorToList(PageIterator<org.eclipse.egit.github.core.User> users)
            throws IOException {
        Iterator<Collection<org.eclipse.egit.github.core.User>> iterator = users.iterator();
        Collection<org.eclipse.egit.github.core.User> collection = iterator.next();
        PaginationAwareArrayList<User> userList = new PaginationAwareArrayList<User>();

        for (org.eclipse.egit.github.core.User user : collection) {
            User userSet = new User(null, user.getLogin());
            userList.add(userSet);
        }

        userList.extractPaginationDataFromIterator(users);
        userList.extractPaginationDataFromCollection(collection);

        return userList;
    }

}
