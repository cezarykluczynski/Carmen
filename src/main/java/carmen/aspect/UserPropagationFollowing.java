package carmen.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import carmen.model.github.User;
import carmen.propagation.UserFollowing;

@Aspect
@Component
public class UserPropagationFollowing {

    @Autowired
    UserFollowing propagationUserFollowing;

    @AfterReturning(
        pointcut = "execution(* carmen.dao.github.UserDAOImpl.createOrUpdateRequestedEntity(..))",
        returning = "userEntity"
    )
    public void userFollowing(JoinPoint joinPoint, User userEntity) {
        propagationUserFollowing.setUserEntity(userEntity);
        propagationUserFollowing.propagate();
    }

}
