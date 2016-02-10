package com.cezarykluczynski.carmen.vcs.git.worker;

import com.cezarykluczynski.carmen.dao.github.RepositoriesClonesDAO;
import com.cezarykluczynski.carmen.dao.github.RepositoriesDAO;
import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.util.exec.Result;
import com.cezarykluczynski.carmen.vcs.git.GitRemote;
import com.cezarykluczynski.carmen.vcs.server.Server;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Log
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
        Result cloneResult = clone(repositoryEntity, cloneDirectory,  repositoryEntity.getFullName());

        if (cloneResult.isSuccessFull()) {
            repositoriesClonesDAO.setStatusToCloned(repositoryCloneEntity);
        } else {
            repositoriesClonesDAO.truncateEntity(server, repositoryCloneEntity);
        }

        log.info("successful: " + cloneResult.isSuccessFull());
        log.info("cloned: " + repositoryCloneEntity.getCloned().toString());
        log.info("getLocationSubdirectory: " + repositoryCloneEntity.getLocationSubdirectory());
        log.info("id: " + repositoryCloneEntity.getId());
        log.info("cloned: " + repositoryCloneEntity.getCloned().toString());
        log.info("error message: " + cloneResult.getErrorMessage());
        log.info("output: " + cloneResult.getOutput());
    }

    protected Result clone(Repository repositoryEntity, String cloneDirectory, String originTargetName) {
        return GitRemote.clone(repositoryEntity.getCloneUrl(), cloneDirectory, originTargetName);
    }

    private String buildCloneDirectory(RepositoryClone repositoryCloneEntity) {
        return buildCloneDirectory(server.getCloneRoot(), repositoryCloneEntity.getLocationDirectory(),
                repositoryCloneEntity.getLocationSubdirectory());
    }

}
