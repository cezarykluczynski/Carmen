package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.RepositoryClone;
import com.cezarykluczynski.carmen.util.exec.Result;
import com.cezarykluczynski.carmen.util.exec.exception.MkDirException;
import com.cezarykluczynski.carmen.util.filesystem.Directory;
import com.cezarykluczynski.carmen.vcs.git.util.DirectoryNameGenerator;
import com.cezarykluczynski.carmen.vcs.server.Server;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Repository
public class RepositoriesClonesDAOImpl implements RepositoriesClonesDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public RepositoryClone createStubEntity(Server server, Repository repositoryEntity) {
        RepositoryClone repositoryCloneEntity = new RepositoryClone();
        String repositoryFullName = repositoryEntity.getFullName();
        repositoryCloneEntity.setLocationDirectory(DirectoryNameGenerator.generateLocationDirectory(repositoryFullName));
        repositoryCloneEntity.setLocationSubdirectory(repositoryFullName);
        repositoryCloneEntity.setRepository(repositoryEntity);
        repositoryCloneEntity.setServerId(server.getServerId());

        try {
            createDirectory(server, repositoryCloneEntity);
        } catch (MkDirException e) {
            return null;
        }

        create(repositoryCloneEntity);

        return repositoryCloneEntity;
    }

    @Transactional
    public RepositoryClone create(RepositoryClone repositoryCloneEntity) {
        Session session = sessionFactory.openSession();
        session.save(repositoryCloneEntity);
        session.flush();
        session.close();
        return repositoryCloneEntity;
    }

    protected void createDirectory(Server server, RepositoryClone repositoryCloneEntity) throws MkDirException {
        List<String> pathElements = new ArrayList<String>();
        pathElements.add(server.getCloneRoot());
        pathElements.add(repositoryCloneEntity.getLocationDirectory());
        pathElements.add(repositoryCloneEntity.getLocationSubdirectory());
        Result commandResult = Directory.create(pathElements);
        if (!commandResult.isSuccessFull()) {
            throw new MkDirException("Directory could not be created.");
        }

    }

}
