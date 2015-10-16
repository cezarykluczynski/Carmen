package com.cezarykluczynski.carmen.aspect.github;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.propagation.github.UserFollowersPropagation;

@Aspect
@Component
public class UserPropagationFollowers {

    @Autowired
    UserFollowersPropagation userFollowersPropagation;

    @AfterReturning(
        pointcut = "execution(* com.cezarykluczynski.carmen.dao.github.UserDAOImpl.createOrUpdateRequestedEntity(..))",
        returning = "userEntity"
    )
    public void userFollowers(JoinPoint joinPoint, User userEntity) {
        userFollowersPropagation.setUserEntity(userEntity);
        userFollowersPropagation.propagate();
    }

}
