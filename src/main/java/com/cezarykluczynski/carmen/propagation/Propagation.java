package com.cezarykluczynski.carmen.propagation;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;

public interface Propagation {

    void propagate();

    void setUserEntity(User userEntity);

}
