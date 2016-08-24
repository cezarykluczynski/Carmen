package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.cron.linguist.executor.LanguagesListUpdateExecutor
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import org.json.JSONObject
import spock.lang.Specification

import javax.ws.rs.core.Response

class LanguagesListUpdateJobImplTest extends Specification {

    private LanguagesListUpdateJobImpl languagesListUpdateJob

    private LanguagesListUpdateExecutor languagesListUpdateExecutor

    private LangsStatsAdapter langsStatsAdapter

    private LanguagesDAO languagesDAO

    def setup() {
        languagesListUpdateExecutor = Mock LanguagesListUpdateExecutor
        langsStatsAdapter = Mock LangsStatsAdapter
        languagesDAO = Mock LanguagesDAO
        languagesListUpdateJob = new LanguagesListUpdateJobImpl(languagesListUpdateExecutor, langsStatsAdapter,
            languagesDAO)
    }

    def "gets status when all languages are persisted"() {
        given:
        List<Language> linguistLanguages = Mock List
        linguistLanguages.size() >> 3
        langsStatsAdapter.getSupportedLanguages() >> linguistLanguages
        languagesDAO.countAll() >> 3

        when:
        Response response = languagesListUpdateJob.getStatus()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        responseStatus == 200
        !responseBody.getBoolean("updatable")
        responseBody.getInt("persistedLanguagesCount") == 3
        responseBody.getInt("linguistLanguagesCount") == 3
    }

    def "gets status when not all languages are persisted"() {
        given:
        List<Language> linguistLanguages = Mock List
        linguistLanguages.size() >> 4
        langsStatsAdapter.getSupportedLanguages() >> linguistLanguages
        languagesDAO.countAll() >> 3

        when:
        Response response = languagesListUpdateJob.getStatus()
        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        responseStatus == 200
        responseBody.getBoolean("updatable")
        responseBody.getInt("persistedLanguagesCount") == 3
        responseBody.getInt("linguistLanguagesCount") == 4
    }

    def "updates status when there are languages to persist"() {
        given:
        List<Language> linguistLanguages = Mock List
        linguistLanguages.size() >> 4
        langsStatsAdapter.getSupportedLanguages() >> linguistLanguages
        languagesDAO.countAll() >>> [3, 4]

        when:
        Response response = languagesListUpdateJob.run()

        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        1 * languagesListUpdateExecutor.run()
        responseStatus == 200
        !responseBody.getBoolean("updatable")
        responseBody.getInt("persistedLanguagesCount") == 4
        responseBody.getInt("linguistLanguagesCount") == 4
    }

    def "does not updates status when there are no languages to persist"() {
        given:
        List<Language> linguistLanguages = Mock List
        linguistLanguages.size() >> 3
        langsStatsAdapter.getSupportedLanguages() >> linguistLanguages
        languagesDAO.countAll() >> 3

        when:
        Response response = languagesListUpdateJob.run()

        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        then:
        0 * languagesListUpdateExecutor.run()
        responseStatus == 200
        !responseBody.getBoolean("updatable")
        responseBody.getInt("persistedLanguagesCount") == 3
        responseBody.getInt("linguistLanguagesCount") == 3
    }

}
