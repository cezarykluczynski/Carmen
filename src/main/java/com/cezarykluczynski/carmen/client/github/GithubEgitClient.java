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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

@Component
public class GithubEgitClient implements GithubClientInterface {

    @Autowired
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
            repositoryListLocal.add(Repository.builder()
                            .id(repository.getId())
                            .parentId(repository.getParent() == null ? null : repository.getParent().getId())
                            .name(repository.getName())
                            .fullName(repository.generateId())
                            .description(repository.getDescription())
                            .homepage(repository.getHomepage())
                            .fork(repository.isFork())
                            .defaultBranch(repository.getMasterBranch())
                            .cloneUrl(repository.getCloneUrl())
                            .created(repository.getCreatedAt())
                            .pushed(repository.getPushedAt())
                            .updated(repository.getUpdatedAt())
                            .build()
            );
        }

        return repositoryListLocal;
    }

    private PaginationAwareArrayList<User> pageIteratorToList(PageIterator<org.eclipse.egit.github.core.User> users)
            throws IOException {
        Iterator<Collection<org.eclipse.egit.github.core.User>> iterator = users.iterator();
        Collection<org.eclipse.egit.github.core.User> collection = iterator.next();
        PaginationAwareArrayList<User> userList = new PaginationAwareArrayList<User>();

        for (org.eclipse.egit.github.core.User user : collection) {
            User userSet = User.builder().login(user.getLogin()).build();
            userList.add(userSet);
        }

        userList.extractPaginationDataFromIterator(users);
        userList.extractPaginationDataFromCollection(collection);

        return userList;
    }

}
