package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github;

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.UsersImportCron;
import com.cezarykluczynski.carmen.admin.web.endpoint.dto.UsersImportCronOverviewDTO;
import com.cezarykluczynski.carmen.cron.DatabaseManageableTask;
import com.cezarykluczynski.carmen.dao.github.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Profile("admin-panel")
public class UsersImportCronImpl implements UsersImportCron {

    private UserDAO userDao;

    private DatabaseManageableTask usersImportTask;

    @Autowired
    public UsersImportCronImpl(UserDAO userDao,
                               @Qualifier("usersImportTask") DatabaseManageableTask usersImportTask) {
        this.userDao = userDao;
        this.usersImportTask = usersImportTask;
    }

    @Override
    public Response get() {
        return Response.ok(UsersImportCronOverviewDTO.builder()
                .highestGitHubUserId(userDao.findHighestGitHubUserId())
                .enabled(usersImportTask.isEnabled())
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
        return Response.ok(UsersImportCronOverviewDTO.builder().enabled(status).build()).build();
    }
}
