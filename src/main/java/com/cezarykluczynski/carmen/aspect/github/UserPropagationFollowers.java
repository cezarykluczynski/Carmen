package com.cezarykluczynski.carmen.aspect.github;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation;

@Aspect
@Component
public class UserPropagationFollowers {

    private UserFollowersPropagation userFollowersPropagation;

    @Autowired
    public UserPropagationFollowers(UserFollowersPropagation userFollowersPropagation) {
        this.userFollowersPropagation = userFollowersPropagation;
    }

    @AfterReturning(
        pointcut = "execution(* com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model." +
                "repository.UserRepositoryImpl.createOrUpdateRequestedEntity(..))",
        returning = "userEntity"
    )
    public void userFollowers(JoinPoint joinPoint, User userEntity) {
        userFollowersPropagation.setUserEntity(userEntity);
        userFollowersPropagation.propagate();
    }

}
