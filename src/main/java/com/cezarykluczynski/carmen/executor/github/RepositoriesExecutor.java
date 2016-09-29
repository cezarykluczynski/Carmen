package com.cezarykluczynski.carmen.executor.github;

import com.cezarykluczynski.carmen.client.github.GithubClient;
import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.propagation.model.repository.RepositoriesRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.RepositoryRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository;
import com.cezarykluczynski.carmen.set.github.RepositoryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class RepositoriesExecutor implements Executor {

    private PendingRequestRepository pendingRequestRepository;

    private UserRepository userRepository;

    private RepositoryRepository repositoryRepository;

    private RepositoriesRepository repositoriesRepository;

    private GithubClient githubClient;

    @Autowired
    public RepositoriesExecutor(PendingRequestRepository pendingRequestRepository, UserRepository userRepository,
            RepositoriesRepository repositoriesRepository, RepositoryRepository repositoryRepository,
            GithubClient githubClient) {
        this.pendingRequestRepository = pendingRequestRepository;
        this.userRepository = userRepository;
        this.repositoriesRepository = repositoriesRepository;
        this.repositoryRepository = repositoryRepository;
        this. githubClient = githubClient;
    }

    public void execute(PendingRequest pendingRequest) throws IOException {
        String login = (String) pendingRequest.getPathParams().get("login");
        User userEntity = userRepository.findByLogin(login);
        List<RepositoryDTO> repositoriesSetList = githubClient.getRepositories(login);
        repositoryRepository.refresh(userEntity, repositoriesSetList);
        repositoriesRepository.moveToSleepPhaseUsingUserEntity(userEntity);
        pendingRequestRepository.delete(pendingRequest);
    }

}
