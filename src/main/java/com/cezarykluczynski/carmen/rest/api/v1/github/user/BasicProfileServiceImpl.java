package com.cezarykluczynski.carmen.rest.api.v1.github.user;

import com.cezarykluczynski.carmen.dao.github.UserDAO;
import com.cezarykluczynski.carmen.rest.dto.api.v1.github.error.Error404ResponseDTO;
import com.cezarykluczynski.carmen.rest.dto.api.v1.github.user.BasicProfileDTO;
import com.cezarykluczynski.carmen.model.github.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service("basicProfileService")
public class BasicProfileServiceImpl implements BasicProfileService {

    UserDAO githubUserDAOImpl;

    @Autowired
    public BasicProfileServiceImpl(UserDAO githubUserDAOImpl) {
        this.githubUserDAOImpl = githubUserDAOImpl;
    }

    @Override
    public Response get(String login) {
        User userEntity = githubUserDAOImpl.findByLogin(login);

        if (null == userEntity) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new Error404ResponseDTO()).build();
        }

        return Response.ok().entity(
                BasicProfileDTO.builder()
                        .login(userEntity.getLogin())
                        .name(userEntity.getName())
                        .avatarUrl(userEntity.getAvatarUrl())
                        .company(userEntity.getCompany())
                        .blog(userEntity.getBlog())
                        .location(userEntity.getLocation())
                        .email(userEntity.getEmail())
                        .bio(userEntity.getBio())
                        .hireable(userEntity.isHireable())
                        .build()
        ).build();
    }

}
