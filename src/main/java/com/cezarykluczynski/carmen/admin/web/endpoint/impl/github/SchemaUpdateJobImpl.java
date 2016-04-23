package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github;

import com.cezarykluczynski.carmen.admin.web.endpoint.api.github.SchemaUpdateJob;
import com.cezarykluczynski.carmen.admin.web.endpoint.dto.SchemaUpdateStatusDTO;
import com.cezarykluczynski.carmen.cron.DatabaseManageableTask;
import com.cezarykluczynski.carmen.cron.languages.executor.SchemaUpdateExecutor;
import com.cezarykluczynski.carmen.cron.languages.util.SchemaUpdateFilesStateHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Profile("admin-panel")
public class SchemaUpdateJobImpl implements SchemaUpdateJob {

    private SchemaUpdateExecutor schemaUpdateExecutor;

    private DatabaseManageableTask schemaUpdateTask;

    private SchemaUpdateFilesStateHelper schemaUpdateFilesStateHelper;

    @Autowired
    public SchemaUpdateJobImpl(SchemaUpdateExecutor schemaUpdateExecutor,
                               @Qualifier("languagesSchemaUpdateTask") DatabaseManageableTask schemaUpdateTask,
                               SchemaUpdateFilesStateHelper schemaUpdateFilesStateHelper) {
        this.schemaUpdateExecutor = schemaUpdateExecutor;
        this.schemaUpdateTask = schemaUpdateTask;
        this.schemaUpdateFilesStateHelper = schemaUpdateFilesStateHelper;
    }

    @Override
    public Response getStatus() {
        return Response.ok(SchemaUpdateStatusDTO.builder()
                .saved(schemaUpdateFilesStateHelper.hasFilesChanged())
                .updated(schemaUpdateTask.getUpdated())
                .enabled(schemaUpdateTask.isEnabled())
                .running(schemaUpdateTask.isRunning())
                .build()).build();
    }

    @Override
    public Response run() {
        schemaUpdateExecutor.run();
        return Response.ok(SchemaUpdateStatusDTO.builder()
                .saved(schemaUpdateFilesStateHelper.hasFilesChanged())
                .updated(schemaUpdateTask.getUpdated())
                .build()).build();
    }
}
