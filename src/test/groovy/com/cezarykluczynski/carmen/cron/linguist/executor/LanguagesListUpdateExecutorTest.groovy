package com.cezarykluczynski.carmen.cron.linguist.executor

import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.data.language.model.entity.Language
import com.cezarykluczynski.carmen.data.language.model.repository.LanguageRepository
import com.cezarykluczynski.carmen.lang.stats.adapter.LangsStatsAdapter
import org.springframework.beans.factory.annotation.Autowired

class LanguagesListUpdateExecutorTest extends IntegrationTest {

    @Autowired
    LanguagesListUpdateExecutor languagesListUpdateExecutor

    @Autowired
    LanguageRepository languageRepository

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
        List<Language> languagesList = languageRepository.findAll()
        Language languageBison = languagesList.stream().filter({ language -> language.getName() == "Bison" }).findFirst().get()
        Language languageYacc = languagesList.stream().filter({ language -> language.getName() == "Yacc" }).findFirst().get()
        Language languageColdFusion = languagesList.stream().filter({ language -> language.getName() == "ColdFusion" }).findFirst().get()

        then:
        linguistLanguagesCount == languagesList.size()
        languageBison.getLinguistParent() == languageYacc
        languageColdFusion.getLinguistParent() == null
    }

}
