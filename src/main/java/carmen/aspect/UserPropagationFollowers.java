package carmen.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import carmen.model.github.User;
import carmen.propagation.UserFollowers;

@Aspect
@Component
public class UserPropagationFollowers {

    @Autowired
    UserFollowers propagationUserFollowers;

    @AfterReturning(
        pointcut = "execution(* carmen.dao.github.UserDAOImpl.createOrUpdateRequestedEntity(..))",
        returning = "userEntity"
    )
    public void userFollowers(JoinPoint joinPoint, User userEntity) {
        propagationUserFollowers.setUserEntity(userEntity);
        propagationUserFollowers.propagate();
    }

}