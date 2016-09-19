package com.cezarykluczynski.carmen.aspect.github;

import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.propagation.github.UserRepositoriesPropagation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class UserPropagationRepositories {

    UserRepositoriesPropagation userRepositoriesPropagation;

    @Autowired
    public UserPropagationRepositories(UserRepositoriesPropagation userRepositoriesPropagation) {
        this.userRepositoriesPropagation = userRepositoriesPropagation;
    }

    @AfterReturning(
        pointcut = "execution(* com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model" +
            ".repository.UserRepositoryImpl.createOrUpdateRequestedEntity(..))",
        returning = "userEntity"
    )
    public void userRepositories(JoinPoint joinPoint, User userEntity) {
        userRepositoriesPropagation.setUserEntity(userEntity);
        userRepositoriesPropagation.propagate();
    }

}
