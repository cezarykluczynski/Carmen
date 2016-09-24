package com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.Repository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.RepositoryClone;
import com.cezarykluczynski.carmen.util.exec.exception.MkDirException;
import com.cezarykluczynski.carmen.util.exec.result.Result;
import com.cezarykluczynski.carmen.util.factory.NowDateProvider;
import com.cezarykluczynski.carmen.util.filesystem.Directory;
import com.cezarykluczynski.carmen.vcs.git.util.DirectoryNameGenerator;
import com.cezarykluczynski.carmen.vcs.server.Server;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RepositoryCloneRepositoryImpl implements RepositoryCloneRepositoryCustom {

    @Autowired
    private RepositoryCloneRepository repositoryCloneRepository;

    private Server server;

    private NowDateProvider nowDateProvider;

    @Autowired
    public RepositoryCloneRepositoryImpl(Server server, NowDateProvider nowDateProvider) {
        this.server = server;
        this.nowDateProvider = nowDateProvider;
    }

    @Override
    public void setStatusToCloned(RepositoryClone repositoryEntity) {
        Date now = nowDateProvider.createNowDate();
        repositoryEntity.setCloned(now);
        repositoryEntity.setUpdated(now);
        repositoryCloneRepository.save(repositoryEntity);
    }

    @Override
    public RepositoryClone createStubEntity(Server serverToBindTo, Repository repositoryEntity) {
        RepositoryClone repositoryCloneEntity = new RepositoryClone();
        String repositoryFullName = repositoryEntity.getFullName();
        repositoryCloneEntity.setLocationDirectory(
                DirectoryNameGenerator.generateLocationDirectory(repositoryFullName));
        repositoryCloneEntity.setLocationSubdirectory(repositoryFullName);
        repositoryCloneEntity.setRepository(repositoryEntity);
        repositoryCloneEntity.setServerId(serverToBindTo.getServerId());

        // TODO: think of moving directiory creation out of here. GitHubCloneWorker should be the one doing it
        try {
            createDirectory(serverToBindTo, repositoryCloneEntity);
        } catch (MkDirException e) {
            return null;
        }

        return repositoryCloneRepository.save(repositoryCloneEntity);
    }

    @Override
    public RepositoryClone findRepositoryCloneWithCommitsToPersist() {
        final String serverId = server.getServerId();
        RepositoryClone repositoryClone = repositoryCloneRepository
                .findFirstByCommitsStatisticsUntilIsNullAndServerIdOrderByUpdated(serverId);
        if (repositoryClone == null) {
            repositoryClone = repositoryCloneRepository
                    .findFirstByCommitsStatisticsUntilIsNotNullAndServerIdOrderByCommitsStatisticsUntil(serverId);
        }

        return repositoryClone;
    }

    @Override
    public RepositoryClone truncateEntity(Server serverToTruncate, RepositoryClone repositoryCloneEntity) {
        return null;
    }

    protected void createDirectory(Server serverInstance, RepositoryClone repositoryCloneEntity) throws MkDirException {
        List<String> pathElements = Lists.newArrayList(
                serverInstance.getCloneRoot(),
                repositoryCloneEntity.getLocationDirectory(),
                repositoryCloneEntity.getLocationSubdirectory()
        );
        Result commandResult = Directory.create(pathElements);
        if (!commandResult.isSuccessFul()) {
            throw new MkDirException("Directory could not be created.");
        }
    }

}
