package com.cezarykluczynski.carmen.dao.github;

import com.cezarykluczynski.carmen.model.github.Repository;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.List;

public interface RepositoriesDAO {

    public List<Repository> findByUser(User userEntity);

    public void refresh(User userEntity, List<com.cezarykluczynski.carmen.set.github.Repository> repositoriesList);

    public void delete(Repository repositoryEntity);

}
