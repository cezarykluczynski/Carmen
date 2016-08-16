package com.cezarykluczynski.carmen.cron.linguist.executor

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import com.cezarykluczynski.carmen.model.pub.Language
import org.springframework.beans.factory.annotation.Autowired

class LanguagesListUpdateExecutorTest extends IntegrationTest {

    @Autowired
    LanguagesListUpdateExecutor languagesListUpdateExecutor

    @Autowired
    LanguagesDAO languagesDAO

    @Autowired
    LangsStatsAdapter langsStatsAdapter

    private int linguistLanguagesCount

    public void setup() {
        linguistLanguagesCount = langsStatsAdapter.getSupportedLanguages().size()
    }

    def "when being run multiple, persist all languages only once"() {
        given:
        languagesListUpdateExecutor.run()
        languagesListUpdateExecutor.run() // run twice to ensure languages are persisted once

        when:
        List<Language> languagesList = languagesDAO.findAll()
        Language languageBison = languagesList.stream().filter({ language -> language.getName() == "Bison" }).findFirst().get()
        Language languageYacc = languagesList.stream().filter({ language -> language.getName() == "Yacc" }).findFirst().get()
        Language languageColdFusion = languagesList.stream().filter({ language -> language.getName() == "ColdFusion" }).findFirst().get()

        then:
        linguistLanguagesCount == languagesList.size()
        languageBison.getLinguistParent() == languageYacc
        languageColdFusion.getLinguistParent() == null
    }

}
