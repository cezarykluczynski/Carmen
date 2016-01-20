package com.cezarykluczynski.carmen.lang.stats.adapter

import com.cezarykluczynski.carmen.lang.stats.domain.Language
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
import org.testng.annotations.Test

@ContextConfiguration([
    "classpath:spring/database-config.xml",
    "classpath:spring/mvc-core-config.xml",
    "classpath:spring/cron-config.xml"
])
@WebAppConfiguration
public class CLILangsStatsAdapterIntegrationTest extends AbstractTestNGSpringContextTests {

    @Value('${detector.client}')
    private String detectorClient

    private LanguageMapper languageMapper = new LinguistLanguageMapper()

    private CLILangsStatsAdapter cliLangsStatsAdapter

    @BeforeClass
    void setUpClass() {
        if (!detectorClient.equals("cli")) {
            throw new SkipException("Local Ruby not available.")
        }

        cliLangsStatsAdapter = new CLILangsStatsAdapter(languageMapper)
    }

    @Test
    void getSupportedLanguages() {
        List<Language> languageList = cliLangsStatsAdapter.getSupportedLanguages()

        Assert.assertEquals languageList.size(), 385
    }
}
