package com.cezarykluczynski.carmen.cron.linguist.executor

import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import com.cezarykluczynski.carmen.lang.stats.configuration.BeanConfiguration
import com.cezarykluczynski.carmen.model.pub.Language
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

@ContextConfiguration(locations = [
     "classpath:spring/database-config.xml",
     "classpath:spring/mvc-core-config.xml",
     "classpath:spring/cron-config.xml"
], classes = [BeanConfiguration.class], loader = SpringApplicationContextLoader.class)
class LanguagesListUpdateExecutorIntegrationTest extends AbstractTestNGSpringContextTests {

    @Autowired
    LanguagesListUpdateExecutor languagesListUpdateExecutor

    @Autowired
    LanguagesDAO languagesDAO

    @Autowired
    LangsStatsAdapter langsStatsAdapter

    private int linguistLanguagesCount

    @BeforeMethod
    public void setUp() {
        linguistLanguagesCount = langsStatsAdapter.getSupportedLanguages().size()
    }

    @Test
    public void run() {
        languagesListUpdateExecutor.run()
        languagesListUpdateExecutor.run() // run twice to ensure languages are persisted once

        List<Language> languagesList = languagesDAO.findAll()
        Language languageBison = languagesList.stream().filter({ language -> language.getName() == "Bison" }).findFirst().get()
        Language languageYacc = languagesList.stream().filter({ language -> language.getName() == "Yacc" }).findFirst().get()

        Assert.assertEquals linguistLanguagesCount, languagesList.size()
        Assert.assertEquals languageBison.getLinguistParent(), languageYacc
    }

}
