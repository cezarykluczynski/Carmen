package com.cezarykluczynski.carmen.rest.api.v1.github.user;

import com.cezarykluczynski.carmen.pojo.rest.api.v1.github.user.BasicProfilePOJO;

import org.springframework.stereotype.Service;

import javax.ws.rs.PathParam;

@Service("basicProfileService")
public class BasicProfileServiceImpl implements BasicProfileService {

    @Override
    public BasicProfilePOJO get(String login) {
        return new BasicProfilePOJO();
    }

}
