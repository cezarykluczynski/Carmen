package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github;

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.UsersImportCron;
import com.cezarykluczynski.carmen.admin.web.endpoint.dto.UsersImportCronOverviewDTO;
import com.cezarykluczynski.carmen.cron.DatabaseManageableTask;
import com.cezarykluczynski.carmen.integration.vendor.github.com.repository.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Profile("admin-panel")
public class UsersImportCronImpl implements UsersImportCron {

    private UserRepository userRepository;

    private DatabaseManageableTask usersImportTask;

    @Autowired
    public UsersImportCronImpl(UserRepository userRepository,
            @Qualifier("usersImportTask") DatabaseManageableTask usersImportTask) {
        this.userRepository = userRepository;
        this.usersImportTask = usersImportTask;
    }

    @Override
    public Response get() {
        return Response.ok(UsersImportCronOverviewDTO.builder()
                .enabled(usersImportTask.isEnabled())
                .highestGitHubUserId(userRepository.findHighestGitHubUserId())
                .running(usersImportTask.isRunning())
                .build()).build();
    }

    @Override
    public Response updateStatus(boolean status) {
        if (status) {
            usersImportTask.enable();
        } else {
            usersImportTask.disable();
        }

        return Response.ok(UsersImportCronOverviewDTO.builder()
                .enabled(status)
                .highestGitHubUserId(userRepository.findHighestGitHubUserId())
                .running(usersImportTask.isRunning())
                .build()).build();
    }
}
