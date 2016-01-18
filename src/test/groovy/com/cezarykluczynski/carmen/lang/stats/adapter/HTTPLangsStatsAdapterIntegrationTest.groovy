package com.cezarykluczynski.carmen.lang.stats.adapter

import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper
import com.cezarykluczynski.carmen.lang.stats.mapper.LinguistLanguageMapper
import com.cezarykluczynski.carmen.util.network.HTTPClient
import com.cezarykluczynski.carmen.util.network.HTTPJSONClientImpl
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.SkipException
import org.testng.annotations.BeforeClass
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

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

    private HTTPClient httpClient

    @BeforeClass
    void setUpClass() {
        httpClient = new HTTPJSONClientImpl(detectorIp, detectorPort)
        if (!detectorClient.equals("cli") && detectorTestAssumeRunningServer != true) {
            throw new SkipException("Local Ruby server cannot be started, and it's not expected to be running.")
        }
    }

    @BeforeMethod
    void setUp() {
        httpLangsStatsAdapter = new HTTPLangsStatsAdapter(httpClient, languageMapper)
    }

    @Test
    void getSupportedLanguages() {
        List<Language> languageList = httpLangsStatsAdapter.getSupportedLanguages()

        Assert.assertEquals languageList.size(), 385
    }

    @Test
    void describeRepository() {
        Map<Language, LineStat> repositoryDescription =
                httpLangsStatsAdapter.describeRepository(".", "3fe8afa350b369c6c697290f64da6aa996ede153")

        Language java = new Language("Java")
        Language javaScript = new Language("JavaScript")
        Language css = new Language("CSS")
        Language groovy = new Language("Groovy")

        Assert.assertEquals repositoryDescription.size(), 4

        Assert.assertTrue repositoryDescription.get(java).getLines() > 194928 - 10
        Assert.assertTrue repositoryDescription.get(java).getLines() < 194928 + 10

        Assert.assertTrue repositoryDescription.get(javaScript).getLines() > 1854 - 10
        Assert.assertTrue repositoryDescription.get(javaScript).getLines() < 1854 + 10

        Assert.assertTrue repositoryDescription.get(css).getLines() > 318 - 10
        Assert.assertTrue repositoryDescription.get(css).getLines() < 318 + 10

        Assert.assertTrue repositoryDescription.get(groovy).getLines() > 193544 - 10
        Assert.assertTrue repositoryDescription.get(groovy).getLines() < 193544 + 10
    }

}
