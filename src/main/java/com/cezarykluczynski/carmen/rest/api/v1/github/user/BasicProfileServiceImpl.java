package com.cezarykluczynski.carmen.rest.api.v1.github.user;

import com.cezarykluczynski.carmen.dao.github.UserDAOImpl;
import com.cezarykluczynski.carmen.pojo.rest.api.v1.github.user.BasicProfilePOJO;
import com.cezarykluczynski.carmen.model.github.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.PathParam;
import javax.ws.rs.NotFoundException;

@Service("basicProfileService")
public class BasicProfileServiceImpl implements BasicProfileService {

    @Autowired
    UserDAOImpl githubUserDAOImpl;

    @Override
    public BasicProfilePOJO get(String login) {
        User userEntity = githubUserDAOImpl.findByLogin(login);

        if (null == userEntity) {
            throw new NotFoundException();
        }

        return new BasicProfilePOJO(
            userEntity.getLogin(),
            userEntity.getName(),
            userEntity.getAvatarUrl(),
            userEntity.getCompany(),
            userEntity.getBlog(),
            userEntity.getLocation(),
            userEntity.getEmail(),
            userEntity.getBio(),
            userEntity.getHireable()
        );
    }

}
