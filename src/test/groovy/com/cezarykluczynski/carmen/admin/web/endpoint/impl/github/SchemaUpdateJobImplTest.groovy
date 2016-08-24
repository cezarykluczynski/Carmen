package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.cron.DatabaseManageableTask
import com.cezarykluczynski.carmen.cron.languages.executor.SchemaUpdateExecutor
import com.cezarykluczynski.carmen.cron.languages.util.SchemaUpdateFilesStateHelper
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import org.apache.commons.lang.math.RandomUtils
import org.json.JSONObject
import spock.lang.Specification

import javax.ws.rs.core.Response
import java.time.LocalDateTime

public class SchemaUpdateJobImplTest extends Specification {

    private static final LocalDateTime UPDATED = LocalDateTime.of(2016, 1, 1, 0, 0)
    private static final boolean ENABLED = RandomUtils.nextBoolean()
    private static final boolean RUNNING = RandomUtils.nextBoolean()
    private static final boolean HAS_FILES_CHANGED = RandomUtils.nextBoolean()
    private static final String VERSION = "4.8.5"

    private SchemaUpdateExecutor schemaUpdateExecutor

    private DatabaseManageableTask schemaUpdateTask

    private LangsStatsAdapter langsStatsAdapter

    private SchemaUpdateFilesStateHelper schemaUpdateFilesStateHelper

    private SchemaUpdateJobImpl schemaUpdateJob

    def setup() {
        schemaUpdateExecutor = Mock SchemaUpdateExecutor
        schemaUpdateTask = Mock DatabaseManageableTask
        schemaUpdateFilesStateHelper = Mock SchemaUpdateFilesStateHelper
        langsStatsAdapter = Mock LangsStatsAdapter
        schemaUpdateJob = new SchemaUpdateJobImpl(schemaUpdateExecutor, schemaUpdateTask, schemaUpdateFilesStateHelper,
                langsStatsAdapter)

        schemaUpdateTask.isEnabled() >> ENABLED
        schemaUpdateTask.isRunning() >> RUNNING
        schemaUpdateTask.getUpdated() >> UPDATED
        schemaUpdateFilesStateHelper.hasFilesChanged() >> HAS_FILES_CHANGED
        langsStatsAdapter.getLinguistVersion() >> VERSION
    }

    def "gets status"() {
        given:
        schemaUpdateTask.isEnabled() >> ENABLED
        schemaUpdateTask.isRunning() >> RUNNING
        schemaUpdateTask.getUpdated() >> UPDATED
        schemaUpdateFilesStateHelper.hasFilesChanged() >> HAS_FILES_CHANGED

        when:
        Response response = schemaUpdateJob.getStatus()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        responseStatus == 200
        LocalDateTime.parse(responseBody.getString("updated")) == UPDATED
        responseBody.getBoolean("enabled") == ENABLED
        responseBody.getBoolean("running") == RUNNING
        responseBody.getBoolean("saved") == !HAS_FILES_CHANGED
        responseBody.getString("linguistVersion") == VERSION
    }

    def runs() {
        given:
        schemaUpdateTask.isEnabled() >> ENABLED
        schemaUpdateTask.isRunning() >> RUNNING
        schemaUpdateTask.getUpdated() >> UPDATED
        schemaUpdateFilesStateHelper.hasFilesChanged() >> HAS_FILES_CHANGED

        when:
        Response response = schemaUpdateJob.run()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        1 * schemaUpdateExecutor.run()
        responseStatus == 200
        LocalDateTime.parse(responseBody.getString("updated")) == UPDATED
        responseBody.getBoolean("enabled") == ENABLED
        responseBody.getBoolean("running") == RUNNING
        responseBody.getBoolean("saved") == !HAS_FILES_CHANGED
        responseBody.getString("linguistVersion") == VERSION
    }

}
