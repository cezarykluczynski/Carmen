package com.cezarykluczynski.carmen.lang.stats.adapter

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper
import com.cezarykluczynski.carmen.lang.stats.mapper.LinguistLanguageMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.SkipException
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import java.lang.reflect.Field

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
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
        // setup
        injectValidBinPath()

        List<Language> languageList = cliLangsStatsAdapter.getSupportedLanguages()

        Assert.assertEquals languageList.size(), 385
    }

    @Test
    void getSupportedLanguagesInvalid() {
        // setup
        injectInvalidBinPath()

        Assert.assertNull cliLangsStatsAdapter.getSupportedLanguages()
    }


    @Test
    void getLinguistVersion() {
        // setup
        injectValidBinPath()

        String linguistVersion = cliLangsStatsAdapter.getLinguistVersion()

        Assert.assertTrue linguistVersion.matches("\\d\\.\\d\\.\\d")
    }

    @Test
    void getLinguistVersionInvalid() {
        // setup
        injectInvalidBinPath()

        Assert.assertNull cliLangsStatsAdapter.getLinguistVersion()
    }

    @Test
    void describeRepository() {
        // setup
        injectValidBinPath()

        Map<Language, LineStat> repositoryDescription =
                cliLangsStatsAdapter.describeRepository(".", "3fe8afa350b369c6c697290f64da6aa996ede153")

        Assert.assertEquals repositoryDescription.size(), 4
    }

    @Test
    void describeRepositoryInvalid() {
        // setup
        injectInvalidBinPath()

        Assert.assertNull cliLangsStatsAdapter.describeRepository(".", "3fe8afa350b369c6c697290f64da6aa996ede153")
    }

    @Test
    void describeCommit() {
        // setup
        injectValidBinPath()

        Map<Language, LineDiffStat> commitDescription =
                cliLangsStatsAdapter.describeCommit(".", "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1")

        Assert.assertEquals commitDescription.size(), 2
    }

    @Test
    void describeCommitInvalid() {
        // setup
        injectInvalidBinPath()

        Assert.assertNull cliLangsStatsAdapter.describeCommit(".", "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1")
    }

    private void injectInvalidBinPath() {
        injectBinPath "./invalid/bin/path"
    }

    private void injectValidBinPath() {
        injectBinPath "./ruby/bin/lang_stats"
    }

    private injectBinPath(String binPath) {
        Field binPathField = cliLangsStatsAdapter.getClass().getDeclaredField "binPath"
        binPathField.setAccessible true
        binPathField.set cliLangsStatsAdapter, binPath
    }

}
