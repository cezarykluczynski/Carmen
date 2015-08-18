package com.cezarykluczynski.carmen.provider.github;

import java.io.IOException;

import com.cezarykluczynski.carmen.set.github.User;
import com.cezarykluczynski.carmen.set.github.RateLimit;
import com.cezarykluczynski.carmen.util.PaginationAwareArrayList;

import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.UserService;
import org.eclipse.egit.github.core.client.PageIterator;

import java.util.Iterator;
import java.util.Collection;

public class GithubEgitProvider implements GithubProviderInterface {

    public GithubEgitProvider(GitHubClient github) {
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

    public PaginationAwareArrayList<User> getFollowers(String name, Integer limit, Integer offset) throws IOException {
        UserService userService = new UserService(github);
        PaginationAwareArrayList<User> userList = pageIteratorToList(userService.pageFollowers(name, offset, limit));
        userList.addPaginationData(offset, limit);
        return userList;
    }

    public PaginationAwareArrayList<User> getFollowing(String name, Integer limit, Integer offset) throws IOException {
        UserService userService = new UserService(github);
        PaginationAwareArrayList<User> userList = pageIteratorToList(userService.pageFollowing(name, offset, limit));
        userList.addPaginationData(offset, limit);
        return userList;
    }

    private PaginationAwareArrayList<User> pageIteratorToList(PageIterator<org.eclipse.egit.github.core.User> users) throws IOException {
        Iterator<Collection<org.eclipse.egit.github.core.User>> iterator = users.iterator();
        Collection<org.eclipse.egit.github.core.User> collection = iterator.next();
        PaginationAwareArrayList<User> userList = new PaginationAwareArrayList<User>();

        for (org.eclipse.egit.github.core.User user : collection) {
            User userSet = new User(null, user.getLogin());
            userList.add(userSet);
        }

        userList.addPaginationData(users, collection);

        return userList;
    }

}
