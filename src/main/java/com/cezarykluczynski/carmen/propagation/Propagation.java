package com.cezarykluczynski.carmen.propagation;

import com.cezarykluczynski.carmen.model.github.User;

public interface Propagation {

    void propagate();

    void setUserEntity(User userEntity);

}
