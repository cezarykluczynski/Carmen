package com.cezarykluczynski.carmen.aspect.github;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.propagation.github.UserFollowingPropagation;

@Aspect
@Component
public class UserPropagationFollowing {

    UserFollowingPropagation userFollowingPropagation;

    @Autowired
    public UserPropagationFollowing(UserFollowingPropagation userFollowingPropagation) {
        this.userFollowingPropagation = userFollowingPropagation;
    }

    @AfterReturning(
        pointcut = "execution(* com.cezarykluczynski.carmen.dao.github.UserDAOImpl.createOrUpdateRequestedEntity(..))",
        returning = "userEntity"
    )
    public void userFollowing(JoinPoint joinPoint, User userEntity) {
        userFollowingPropagation.setUserEntity(userEntity);
        userFollowingPropagation.propagate();
    }

}
