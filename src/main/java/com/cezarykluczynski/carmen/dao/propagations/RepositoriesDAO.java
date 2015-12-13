package com.cezarykluczynski.carmen.dao.propagations;

import com.cezarykluczynski.carmen.model.propagations.Repositories;
import com.cezarykluczynski.carmen.model.github.User;

import java.util.List;

public interface RepositoriesDAO {

    Repositories findByUser(User userEntity);

    Repositories findOldestPropagationInSleepPhase();

    Repositories create(User userEntity);

    void update(Repositories repositories);

    void delete(Repositories repositories);

    Repositories findById(Long id);

    void moveToRefreshPhase(Repositories repositoriesEntity);

    void moveToSleepPhaseUsingUserEntity(User userEntity);

}
