package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github;

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.SchemaUpdateJob;
import com.cezarykluczynski.carmen.admin.web.endpoint.dto.SchemaUpdateStatusDTO;
import com.cezarykluczynski.carmen.cron.DatabaseManageableTask;
import com.cezarykluczynski.carmen.cron.languages.executor.SchemaUpdateExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Profile("admin-panel")
public class SchemaUpdateJobImpl implements SchemaUpdateJob {

    private SchemaUpdateExecutor schemaUpdateExecutor;

    private DatabaseManageableTask usersImportTask;

    @Autowired
    public SchemaUpdateJobImpl(SchemaUpdateExecutor schemaUpdateExecutor,
                               @Qualifier("languagesSchemaUpdateTask") DatabaseManageableTask usersImportTask) {
        this.schemaUpdateExecutor = schemaUpdateExecutor;
        this.usersImportTask = usersImportTask;
    }

    @Override
    public Response getStatus() {
        return Response.ok(SchemaUpdateStatusDTO.builder()
                .updated(usersImportTask.getUpdated())
                .enabled(usersImportTask.isEnabled())
                .running(usersImportTask.isRunning())
                .build()).build();
    }

    @Override
    public Response run() {
        schemaUpdateExecutor.run();
        return Response.ok(SchemaUpdateStatusDTO.builder()
                .updated(usersImportTask.getUpdated())
                .build()).build();
    }
}
