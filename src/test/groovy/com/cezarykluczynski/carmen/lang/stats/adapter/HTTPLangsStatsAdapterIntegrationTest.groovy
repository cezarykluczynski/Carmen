package com.cezarykluczynski.carmen.lang.stats.adapter

import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper
import com.cezarykluczynski.carmen.lang.stats.mapper.LinguistLanguageMapper
import com.cezarykluczynski.carmen.util.network.HTTPClient
import com.cezarykluczynski.carmen.util.network.HTTPJSONClientImpl
import com.cezarykluczynski.carmen.util.network.HTTPRequestException
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.SkipException
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import java.lang.reflect.Field

import static org.mockito.Matchers.anyMap
import static org.mockito.Matchers.anyString
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
@WebAppConfiguration
class HTTPLangsStatsAdapterIntegrationTest extends AbstractTestNGSpringContextTests {

    @Value('${detector.ip}')
    private String detectorIp

    @Value('${detector.port}')
    private int detectorPort

    @Value('${detector.client}')
    private String detectorClient

    @Value('${detector.test.assumeRunningServer}')
    private Boolean detectorTestAssumeRunningServer

    private LanguageMapper languageMapper = new LinguistLanguageMapper()

    private HTTPLangsStatsAdapter httpLangsStatsAdapter

    HTTPClient httpClient

    HTTPClient invalidHttpClient

    @BeforeClass
    void setUpClass() {
        if (!detectorClient.equals("cli") && detectorTestAssumeRunningServer != true) {
            throw new SkipException("Local Ruby server cannot be started, and it's not expected to be running.")
        }

        httpClient = new HTTPJSONClientImpl(detectorIp, detectorPort)

        invalidHttpClient = mock HTTPJSONClientImpl.class
        when invalidHttpClient.get(anyString()) thenThrow new HTTPRequestException(new Throwable())
        when invalidHttpClient.post(anyString(), anyMap()) thenThrow new HTTPRequestException(new Throwable())

        httpLangsStatsAdapter = new HTTPLangsStatsAdapter(httpClient, languageMapper)
    }

    @Test
    void getSupportedLanguages() {
        // setup
        injectValidHttpClient()

        List<Language> languageList = httpLangsStatsAdapter.getSupportedLanguages()

        Assert.assertEquals languageList.size(), 385
    }

    @Test
    void getSupportedLanguagesInvalid() {
        // setup
        injectInvalidHttpClient()

        Assert.assertNull httpLangsStatsAdapter.getSupportedLanguages()
    }

    @Test
    void describeRepository() {
        // setup
        injectValidHttpClient()

        Map<Language, LineStat> repositoryDescription =
                httpLangsStatsAdapter.describeRepository(".", "3fe8afa350b369c6c697290f64da6aa996ede153")

        Assert.assertEquals repositoryDescription.size(), 4
    }

    @Test
    void describeRepositoryInvalid() {
        // setup
        injectInvalidHttpClient()

        Assert.assertNull httpLangsStatsAdapter.describeRepository(".", "3fe8afa350b369c6c697290f64da6aa996ede153")
    }

    @Test
    void describeCommit() {
        // setup
        injectValidHttpClient()

        Map<Language, LineDiffStat> commitDescription =
                httpLangsStatsAdapter.describeCommit(".", "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1")

        Assert.assertEquals commitDescription.size(), 2
    }

    @Test
    void describeCommitInvalid() {
        // setup
        injectInvalidHttpClient()

        Assert.assertNull httpLangsStatsAdapter.describeCommit(".", "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1")
    }

    private void injectValidHttpClient() {
        injectHttpClient httpClient
    }

    private void injectInvalidHttpClient() {
        injectHttpClient invalidHttpClient
    }

    private void injectHttpClient(HTTPClient httpClientImpl) {
        Field binPathField = httpLangsStatsAdapter.getClass().getDeclaredField "httpClient"
        binPathField.setAccessible true
        binPathField.set httpLangsStatsAdapter, httpClientImpl

    }

}
