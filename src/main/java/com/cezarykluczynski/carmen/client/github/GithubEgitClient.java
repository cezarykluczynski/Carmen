package com.cezarykluczynski.carmen.client.github;

import com.cezarykluczynski.carmen.integration.vendor.github.com.api.dto.RateLimitDTO;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.set.github.UserDTO;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Component
public class GithubEgitClient implements GithubClientInterface {

    @Autowired
    public GithubEgitClient(GitHubClient github) {
        this.github = github;
    }

    private GitHubClient github;

    public RateLimitDTO getCoreLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public RateLimitDTO getSearchLimit() throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public UserDTO getUser(String name) throws IOException {
        throw new IOException("Implemented in different provider.");
    }

    public List<Repository> getRepositories(String login) throws IOException {
        RepositoryService repositoryService = new RepositoryService(github);
        List<org.eclipse.egit.github.core.Repository> repositoriesList = repositoryService.getRepositories(login);
        return mapToNativeRepositoriesPOJOList(repositoriesList);
    }

    public PaginationAwareArrayList<UserDTO> getFollowers(String name, Integer limit, Integer offset)
            throws IOException {
        UserService userService = new UserService(github);
        PaginationAwareArrayList<UserDTO> userDTOList =
                pageIteratorToList(userService.pageFollowers(name, offset, limit));
        userDTOList.addPaginationLimitAndOffset(limit, offset);
        return userDTOList;
    }

    public PaginationAwareArrayList<UserDTO> getFollowing(String name, Integer limit, Integer offset)
            throws IOException {
        UserService userService = new UserService(github);
        PaginationAwareArrayList<UserDTO> userDTOList =
                pageIteratorToList(userService.pageFollowing(name, offset, limit));
        userDTOList.addPaginationLimitAndOffset(limit, offset);
        return userDTOList;
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

    private PaginationAwareArrayList<UserDTO> pageIteratorToList(PageIterator<org.eclipse.egit.github.core.User> users)
            throws IOException {
        Iterator<Collection<org.eclipse.egit.github.core.User>> iterator = users.iterator();
        Collection<org.eclipse.egit.github.core.User> collection = iterator.next();
        PaginationAwareArrayList<UserDTO> userDTOList = new PaginationAwareArrayList<UserDTO>();

        for (org.eclipse.egit.github.core.User user : collection) {
            UserDTO userDTOSet = UserDTO.builder().login(user.getLogin()).build();
            userDTOList.add(userDTOSet);
        }

        userDTOList.extractPaginationDataFromIterator(users);
        userDTOList.extractPaginationDataFromCollection(collection);

        return userDTOList;
    }

}
