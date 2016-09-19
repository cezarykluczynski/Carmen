package com.cezarykluczynski.carmen.executor.github;

import com.cezarykluczynski.carmen.cron.model.entity.PendingRequest;
import com.cezarykluczynski.carmen.cron.model.repository.PendingRequestRepository;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.entity.User;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class UserGhostExecutor implements Executor {

    private PendingRequestRepository pendingRequestRepository;

    private UserRepository userRepository;

    @Autowired
    public UserGhostExecutor(PendingRequestRepository pendingRequestRepository, UserRepository userRepository) {
        this.pendingRequestRepository = pendingRequestRepository;
        this.userRepository = userRepository;
    }

    public void execute(PendingRequest pendingRequest) throws IOException {
        String login = (String) pendingRequest.getPathParams().get("login");
        User user = userRepository.createOrUpdateGhostEntity(login);
        linkUsers(pendingRequest, user);
        pendingRequestRepository.delete(pendingRequest);
    }

    private void linkUsers(PendingRequest pendingRequest, User user) {
        Long subjectId = (Long) pendingRequest.getParams().get("link_with");
        String userLinkType = (String) pendingRequest.getParams().get("link_as");
        User subject = userRepository.findById(subjectId);

        if (userLinkType.equals("follower")) {
            userRepository.linkFollowerWithFollowee(user, subject);
        }
        if (userLinkType.equals("followee")) {
            userRepository.linkFollowerWithFollowee(subject, user);
        }
    }

}
