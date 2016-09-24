package com.cezarykluczynski.carmen

import com.cezarykluczynski.carmen.cron.linguist.executor.LanguagesListUpdateExecutor
import com.cezarykluczynski.carmen.data.language.model.repository.LanguageRepository
import org.springframework.beans.factory.annotation.Autowired

class BeforeAll extends IntegrationTest {

    @Autowired
    private LanguagesListUpdateExecutor languagesListUpdateExecutor

    @Autowired
    private LanguageRepository languageRepository

    def "migrations run"() {
        when:
        languagesListUpdateExecutor.run()
        println "Carmen: Languages migration executed."

        then:
        languageRepository.count() > 400
    }

}
