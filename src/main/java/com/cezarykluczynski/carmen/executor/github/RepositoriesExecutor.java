package com.cezarykluczynski.carmen.executor.github;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.dao.apiqueue.PendingRequestDAO;
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO;
import com.cezarykluczynski.carmen.set.github.Repository;
import com.cezarykluczynski.carmen.client.github.GithubClient;

@Component
public class RepositoriesExecutor implements Executor {

    @Autowired
    PendingRequestDAO apiqueuePendingRequestDAOImpl;

    @Autowired
    UserDAO githubUserDAOImpl;

    @Autowired
    RepositoriesDAO githubRepositoriesDAOImpl;

    @Autowired
    com.cezarykluczynski.carmen.dao.propagations.RepositoriesDAO propagationsRepositoriesDAOImpl;

    @Autowired
    GithubClient githubClient;

    public void execute(PendingRequest pendingRequest) throws IOException {
        String login = (String) pendingRequest.getPathParams().get("login");
        User userEntity = githubUserDAOImpl.findByLogin(login);
        List<Repository> repositoriesSetList = githubClient.getRepositories(login);
        githubRepositoriesDAOImpl.refresh(userEntity, repositoriesSetList);
        propagationsRepositoriesDAOImpl.moveToSleepPhaseUsingUserEntity(userEntity);
        apiqueuePendingRequestDAOImpl.delete(pendingRequest);
    }

}
