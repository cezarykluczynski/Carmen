package com.cezarykluczynski.carmen.lang.stats.adapter

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
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

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
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

        Assert.assertEquals languageList.size(), 401
    }

    @Test
    void getSupportedLanguagesInvalid() {
        // setup
        injectInvalidHttpClient()

        Assert.assertNull httpLangsStatsAdapter.getSupportedLanguages()
    }


    @Test
    void getLinguistVersion() {
        // setup
        injectValidHttpClient()

        String linguistVersion = httpLangsStatsAdapter.getLinguistVersion()

        Assert.assertTrue linguistVersion.matches("\\d\\.\\d\\.\\d")
    }

    @Test
    void getLinguistVersionInvalid() {
        // setup
        injectInvalidHttpClient()

        Assert.assertNull httpLangsStatsAdapter.getLinguistVersion()
    }

    @Test
    void describeRepository() {
        // setup
        injectValidHttpClient()

        final String commitHash = "3fe8afa350b369c6c697290f64da6aa996ede153"

        RepositoryDescription repositoryDescription = httpLangsStatsAdapter.describeRepository(".", commitHash)

        Assert.assertEquals repositoryDescription.getLineStats().size(), 4
        Assert.assertEquals repositoryDescription.getCommitHash(), commitHash
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

        final String commitHash = "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1"

        CommitDescription commitDescription =
                httpLangsStatsAdapter.describeCommit(".", commitHash)

        Assert.assertEquals commitDescription.getLineDiffStats().size(), 2
        Assert.assertEquals commitDescription.getCommitHash(), commitHash
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
