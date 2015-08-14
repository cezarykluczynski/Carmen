package com.cezarykluczynski.carmen.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cezarykluczynski.carmen.dao.propagations.UserFollowersDAOImpl;
import com.cezarykluczynski.carmen.dao.propagations.UserFollowingDAOImpl;
import com.cezarykluczynski.carmen.model.github.User;
import com.cezarykluczynski.carmen.model.apiqueue.PendingRequest;
import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.propagations.UserFollowing;

@Aspect
@Component
public class UserGhostEndDiscoverPhase {

    @Autowired
    com.cezarykluczynski.carmen.propagation.UserFollowers propagationUserFollowers;

    @Autowired
    com.cezarykluczynski.carmen.propagation.UserFollowing propagationUserFollowing;

    @After("execution(* com.cezarykluczynski.carmen.executor.UserGhost.execute(..))")
    public void afterExecute(JoinPoint joinPoint) {
        PendingRequest pendingRequest = (PendingRequest) joinPoint.getArgs()[0];
        String userLinkType = (String) pendingRequest.getParams().get("link_as");

        if (userLinkType.equals("follower")) {
            propagationUserFollowers.tryToMoveToReportPhase(pendingRequest);
        } else if (userLinkType.equals("followee")) {
            propagationUserFollowing.tryToMoveToReportPhase(pendingRequest);
        }
    }

}
