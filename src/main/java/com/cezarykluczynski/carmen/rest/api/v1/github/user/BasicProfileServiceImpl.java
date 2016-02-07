package com.cezarykluczynski.carmen.rest.api.v1.github.user;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.pojo.rest.api.v1.github.error.Error404ResponsePOJO;
import com.cezarykluczynski.carmen.pojo.rest.api.v1.github.user.BasicProfilePOJO;
import com.cezarykluczynski.carmen.model.github.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service("basicProfileService")
public class BasicProfileServiceImpl implements BasicProfileService {

    @Autowired
    UserDAO githubUserDAOImpl;

    @Override
    public Response get(String login) {
        User userEntity = githubUserDAOImpl.findByLogin(login);

        if (null == userEntity) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new Error404ResponsePOJO()).build();
        }

        return Response.ok(
            new BasicProfilePOJO(
                userEntity.getLogin(),
                userEntity.getName(),
                userEntity.getAvatarUrl(),
                userEntity.getCompany(),
                userEntity.getBlog(),
                userEntity.getLocation(),
                userEntity.getEmail(),
                userEntity.getBio(),
                userEntity.getHireable()
            )
        ).build();
    }

}
