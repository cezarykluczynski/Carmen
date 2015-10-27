package com.cezarykluczynski.carmen.executor.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAOImpl;
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAOImpl;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.provider.github.GithubProvider;

@Component
public class RepositoriesExecutor implements Executor {

    @Autowired
    PendingRequestDAOImpl apiqueuePendingRequestDAOImpl;

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Autowired
    RepositoriesDAOImpl propagationsRepositoriesDAOImpl;

    @Autowired
    GithubProvider githubProvider;

    public void execute(PendingRequest pendingRequest) throws IOException {
        String login = (String) pendingRequest.getPathParams().get("login");
        User userEntity = githubUserDAOImpl.findByLogin(login);
        List<Repository> repositoriesSetList = githubProvider.getRepositories(login);
        propagationsRepositoriesDAOImpl.refresh(userEntity, repositoriesSetList);
        apiqueuePendingRequestDAOImpl.delete(pendingRequest);
    }

}
