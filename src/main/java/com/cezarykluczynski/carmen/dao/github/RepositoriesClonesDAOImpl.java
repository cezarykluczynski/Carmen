package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.util.DateUtil;
import com.cezarykluczynski.carmen.util.exec.exception.MkDirException;
import com.cezarykluczynski.carmen.util.exec.result.Result;
import com.cezarykluczynski.carmen.util.filesystem.Directory;
import com.cezarykluczynski.carmen.vcs.git.util.DirectoryNameGenerator;
import com.cezarykluczynski.carmen.vcs.server.Server;
import com.google.common.collect.Lists;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@org.springframework.stereotype.Repository
public class RepositoriesClonesDAOImpl implements RepositoriesClonesDAO {

    private SessionFactory sessionFactory;

    private Server server;

    @Autowired
    public RepositoriesClonesDAOImpl(SessionFactory sessionFactory, Server server) {
        this.sessionFactory = sessionFactory;
        this.server = server;
    }

    @Override
    public RepositoryClone createStubEntity(Server serverInstance, Repository repositoryEntity) {
        RepositoryClone repositoryCloneEntity = new RepositoryClone();
        String repositoryFullName = repositoryEntity.getFullName();
        repositoryCloneEntity.setLocationDirectory(
                DirectoryNameGenerator.generateLocationDirectory(repositoryFullName));
        repositoryCloneEntity.setLocationSubdirectory(repositoryFullName);
        repositoryCloneEntity.setRepository(repositoryEntity);
        repositoryCloneEntity.setServerId(serverInstance.getServerId());

        try {
            createDirectory(serverInstance, repositoryCloneEntity);
        } catch (MkDirException e) {
            return null;
        }

        return create(repositoryCloneEntity);
    }

    public RepositoryClone findByRepositoryEntity(Repository repositoryEntity) {
        Session session = sessionFactory.openSession();

        List<RepositoryClone> list = session.createQuery(
            "SELECT rc FROM github.RepositoryClone rc WHERE rc.repository = :repository"
        )
            .setEntity("repository", repositoryEntity)
            .setMaxResults(1)
            .list();
        session.close();

        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    public RepositoryClone findRepositoryCloneWithCommitsToPersist() {
        Session session = sessionFactory.openSession();

        List<RepositoryClone> list = session.createQuery("SELECT rc FROM github.RepositoryClone rc " +
                "WHERE rc.commitsStatisticsUntil = null AND rc.serverId = :serverId ORDER BY rc.updated")
                .setParameter("serverId", server.getServerId())
                .setMaxResults(1)
                .list();

        if (list.size() == 0) {
                list = session.createQuery("SELECT rc FROM github.RepositoryClone rc " +
                        "WHERE rc.commitsStatisticsUntil != null AND rc.serverId = :serverId " +
                        "ORDER BY rc.commitsStatisticsUntil")
                    .setParameter("serverId", server.getServerId())
                    .setMaxResults(1)
                    .list();
        }

        session.close();

        return list.size() == 1 ? list.get(0) : null;
    }

    @Override
    public RepositoryClone truncateEntity(Server serverInstance, RepositoryClone repositoryCloneEntity) {
        return null;
    }

    @Override
    public void setStatusToCloned(RepositoryClone repositoryEntity) {
        Date now = DateUtil.now();
        repositoryEntity.setCloned(now);
        repositoryEntity.setUpdated(now);
        update(repositoryEntity);
    }

    @Override
    @Transactional
    public RepositoryClone create(RepositoryClone repositoryCloneEntity) {
        Session session = sessionFactory.openSession();
        session.save(repositoryCloneEntity);
        session.flush();
        session.close();
        return repositoryCloneEntity;
    }

    @Override
    @Transactional
    public RepositoryClone update(RepositoryClone repositoryCloneEntity) {
        Session session = sessionFactory.openSession();
        session.update(repositoryCloneEntity);
        session.flush();
        session.close();
        return  repositoryCloneEntity;
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
