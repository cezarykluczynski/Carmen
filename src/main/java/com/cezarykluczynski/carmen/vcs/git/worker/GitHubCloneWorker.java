package com.cezarykluczynski.carmen.vcs.git.worker;

import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO;
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO;
import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.util.exec.Result;
import com.cezarykluczynski.carmen.vcs.git.GitRemote;
import com.cezarykluczynski.carmen.vcs.server.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GitHubCloneWorker extends AbstractCloneWorker implements Runnable {

    @Autowired
    RepositoriesDAO repositoriesDAO;

    @Autowired
    RepositoriesClonesDAO repositoriesClonesDAO;

    @Autowired
    Server server;

    @Override
    public void run() {
        Repository repositoryEntity = repositoriesDAO.findNotForkedRepositoryWithoutClone();
        if (repositoryEntity == null) {
            return;
        }

        RepositoryClone repositoryCloneEntity = repositoriesClonesDAO.createStubEntity(server, repositoryEntity);
        String cloneDirectory = buildCloneDirectory(repositoryCloneEntity);
        Result cloneResult = clone(repositoryEntity, cloneDirectory);

        if (cloneResult.isSuccessFull()) {
            repositoriesClonesDAO.setStatusToCloned(repositoryCloneEntity);
        } else {
            repositoriesClonesDAO.truncateEntity(server, repositoryCloneEntity);
        }
    }

    @Override
    protected Result clone(Repository repositoryEntity, String cloneDirectory) {
        return GitRemote.clone(repositoryEntity.getCloneUrl(), cloneDirectory);
    }

    private String buildCloneDirectory(RepositoryClone repositoryCloneEntity) {
        return buildCloneDirectory(server.getCloneRoot(), repositoryCloneEntity.getLocationDirectory(),
                repositoryCloneEntity.getLocationSubdirectory());
    }

}
