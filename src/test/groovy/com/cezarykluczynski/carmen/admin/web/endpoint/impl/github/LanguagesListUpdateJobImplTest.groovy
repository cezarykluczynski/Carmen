package com.cezarykluczynski.carmen.admin.web.endpoint.impl.github

import com.cezarykluczynski.carmen.cron.linguist.executor.LanguagesListUpdateExecutor
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import org.json.JSONObject
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import javax.ws.rs.core.Response

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class LanguagesListUpdateJobImplTest {

    private LanguagesListUpdateJobImpl languagesListUpdateJob

    private LanguagesListUpdateExecutor languagesListUpdateExecutor

    private LangsStatsAdapter langsStatsAdapter

    private LanguagesDAO languagesDAO

    @BeforeMethod
    def setup() {
        languagesListUpdateExecutor = mock LanguagesListUpdateExecutor.class
        langsStatsAdapter = mock LangsStatsAdapter.class
        languagesDAO = mock LanguagesDAO.class
        languagesListUpdateJob = new LanguagesListUpdateJobImpl(languagesListUpdateExecutor, langsStatsAdapter,
            languagesDAO)
    }

    @Test
    void "gets status when all languages are persisted"() {
        List<Language> linguistLanguages = mock(List.class)
        when linguistLanguages.size() thenReturn 3
        when langsStatsAdapter.getSupportedLanguages() thenReturn linguistLanguages

        when languagesDAO.countAll() thenReturn 3

        Response response = languagesListUpdateJob.getStatus()

        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertFalse responseBody.getBoolean("updatable")
        Assert.assertEquals(responseBody.getInt("persistedLanguagesCount"), 3)
        Assert.assertEquals(responseBody.getInt("linguistLanguagesCount"), 3)
    }

    @Test
    void "gets status when not all languages are persisted"() {
        List<Language> linguistLanguages = mock(List.class)
        when linguistLanguages.size() thenReturn 4
        when langsStatsAdapter.getSupportedLanguages() thenReturn linguistLanguages

        when languagesDAO.countAll() thenReturn 3

        Response response = languagesListUpdateJob.getStatus()

        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertTrue responseBody.getBoolean("updatable")
        Assert.assertEquals(responseBody.getInt("persistedLanguagesCount"), 3)
        Assert.assertEquals(responseBody.getInt("linguistLanguagesCount"), 4)
    }

    @Test
    void "updates status when there are languages to persist"() {
        List<Language> linguistLanguages = mock(List.class)
        when linguistLanguages.size() thenReturn 4
        when langsStatsAdapter.getSupportedLanguages() thenReturn linguistLanguages

        when languagesDAO.countAll() thenReturn 3, 4

        Response response = languagesListUpdateJob.run()

        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertFalse responseBody.getBoolean("updatable")
        Assert.assertEquals(responseBody.getInt("persistedLanguagesCount"), 4)
        Assert.assertEquals(responseBody.getInt("linguistLanguagesCount"), 4)
        verify languagesListUpdateExecutor run()
    }

    @Test
    void "does not updates status when there are no languages to persist"() {
        List<Language> linguistLanguages = mock(List.class)
        when linguistLanguages.size() thenReturn 3
        when langsStatsAdapter.getSupportedLanguages() thenReturn linguistLanguages

        when languagesDAO.countAll() thenReturn 3

        Response response = languagesListUpdateJob.run()

        int responseStatus = response.getStatus()
        JSONObject responseBody = new JSONObject(response.getEntity())

        Assert.assertEquals responseStatus, 200
        Assert.assertFalse responseBody.getBoolean("updatable")
        Assert.assertEquals(responseBody.getInt("persistedLanguagesCount"), 3)
        Assert.assertEquals(responseBody.getInt("linguistLanguagesCount"), 3)
        verify(languagesListUpdateExecutor, never()).run()
    }

}
