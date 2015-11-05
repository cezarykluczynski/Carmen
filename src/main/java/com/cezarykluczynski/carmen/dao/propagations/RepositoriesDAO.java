package com.cezarykluczynski.carmen.dao.propagations;

import com.cezarykluczynski.carmen.model.propagations.Repositories;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.List;

public interface RepositoriesDAO {

    public Repositories findByUser(User userEntity);

    public Repositories findOldestPropagationInSleepPhase();

    public Repositories create(User userEntity);

    public void update(Repositories repositories);

    public void delete(Repositories repositories);

    public Repositories findById(Long id);

    public void moveToSleepPhaseUsingUserEntity(User userEntity);
}
