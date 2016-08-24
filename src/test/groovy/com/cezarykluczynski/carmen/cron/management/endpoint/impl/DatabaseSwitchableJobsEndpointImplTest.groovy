package com.cezarykluczynski.carmen.cron.management.endpoint.impl

import com.cezarykluczynski.carmen.cron.management.dto.DatabaseSwitchableJobDTO
import com.cezarykluczynski.carmen.cron.management.service.DatabaseSwitchableJobsService
import com.google.common.collect.Lists
import org.json.JSONObject
import spock.lang.Specification

import javax.ws.rs.core.Response

class DatabaseSwitchableJobsEndpointImplTest extends Specification {

    private static final String NAME = "name"

    private DatabaseSwitchableJobsEndpointImpl endpoint

    private DatabaseSwitchableJobsService jobsService

    def setup() {
        jobsService = Mock DatabaseSwitchableJobsService
        endpoint = new DatabaseSwitchableJobsEndpointImpl(jobsService)
    }

    def "gets all jobs"() {
        when:
        Response response = endpoint.getAll()
        List<DatabaseSwitchableJobDTO> entity = (List<DatabaseSwitchableJobDTO>) response.getEntity()

        then:
        1 * jobsService.getAll() >> Lists.newArrayList(Mock(DatabaseSwitchableJobDTO))
        response.getStatus() == 200
        entity.size() == 1
    }

    def "updates list"() {
        when:
        Response response = endpoint.updateList()

        then:
        1 * jobsService.updateList()
        response.getStatus() == 200
    }

    def "enables job by name"() {
        when:
        Response response = endpoint.enable NAME
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        1 * jobsService.enable(_) >> {args ->
            assert ((DatabaseSwitchableJobDTO) args[0]).name == NAME
        }
        response.getStatus() == 200
        responseBody.getString("name") == NAME
    }

    def "disables job by name"() {
        when:
        Response response = endpoint.disable NAME
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        1 * jobsService.disable(_) >> {args ->
            assert ((DatabaseSwitchableJobDTO) args[0]).name == NAME
        }
        response.getStatus() == 200
        responseBody.getString("name") == NAME
    }

}
