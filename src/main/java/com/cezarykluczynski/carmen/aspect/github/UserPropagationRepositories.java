package com.cezarykluczynski.carmen.aspect.github;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation;

@Aspect
@Component
public class UserPropagationRepositories {

    @Autowired
    UserRepositoriesPropagation userRepositoriesPropagation;

    @AfterReturning(
        pointcut = "execution(* com.cezarykluczynski.carmen.dao.github.UserDAOImpl.createOrUpdateRequestedEntity(..))",
        returning = "userEntity"
    )
    public void userRepositories(JoinPoint joinPoint, User userEntity) {
        userRepositoriesPropagation.setUserEntity(userEntity);
        userRepositoriesPropagation.propagate();
    }

}
