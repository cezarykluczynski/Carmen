package com.cezarykluczynski.carmen.propagation;

import com.cezarykluczynski.carmen.model.github.User;

public interface Propagation {

    public void propagate();

    public void setUserEntity(User userEntity);

}
