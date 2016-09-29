package com.cezarykluczynski.carmen.client.github;

import com.cezarykluczynski.carmen.common.util.pagination.calculator.PagerCalculator;
import com.cezarykluczynski.carmen.common.util.pagination.dto.Slice;
import com.cezarykluczynski.carmen.common.util.pagination.dto.Pager;
import com.cezarykluczynski.carmen.common.util.pagination.factory.PagerFactory;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;
import com.cezarykluczynski.carmen.set.github.UserDTO;
import com.google.common.collect.Lists;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@Component
public class GithubEgitClient implements GithubClientInterface {

    private GitHubClient gitHubClient;

    @Autowired
    public GithubEgitClient(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public List<RepositoryDTO> getRepositories(String login) throws IOException {
        RepositoryService repositoryService = new RepositoryService(gitHubClient);
        List<Repository> repositoriesList = repositoryService.getRepositories(login);
        return mapToNativeRepositoriesPOJOList(repositoriesList);
    }

    public Slice<UserDTO> getFollowers(String name, Pager pager) throws IOException {
        UserService userService = new UserService(gitHubClient);
        PageIterator<User> userPageIterator = userService
                .pageFollowers(name, PagerCalculator.toGitHubApiPageNumber(pager), pager.getItemsPerPage());
        return toPage(userPageIterator, pager);
    }

    public Slice<UserDTO> getFollowing(String name, Pager pager) throws IOException {
        UserService userService = new UserService(gitHubClient);
        PageIterator<User> userPageIterator = userService
                .pageFollowing(name, PagerCalculator.toGitHubApiPageNumber(pager), pager.getItemsPerPage());
        return toPage(userPageIterator, pager);
    }

    private List<RepositoryDTO> mapToNativeRepositoriesPOJOList(
            List<Repository> repositoriesListRemote) {
        List<RepositoryDTO> repositoryDTOListLocal = Lists.newArrayList();

        for (Repository repository : repositoriesListRemote) {
            repositoryDTOListLocal.add(RepositoryDTO.builder()
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

        return repositoryDTOListLocal;
    }

    private Slice<UserDTO> toPage(PageIterator<org.eclipse.egit.github.core.User> users, Pager requestPager)
            throws IOException {
        Iterator<Collection<org.eclipse.egit.github.core.User>> iterator = users.iterator();
        Collection<org.eclipse.egit.github.core.User> collection = iterator.next();
        List<UserDTO> userDTOList = Lists.newArrayList();

        for (org.eclipse.egit.github.core.User user : collection) {
            UserDTO userDTOSet = UserDTO.builder().login(user.getLogin()).build();
            userDTOList.add(userDTOSet);
        }

        return Slice.of(userDTOList, PagerFactory.of(users, requestPager));
    }

}
