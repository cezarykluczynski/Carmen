package com.cezarykluczynski.carmen

import com.cezarykluczynski.carmen.cron.linguist.executor.LanguagesListUpdateExecutor
import org.springframework.beans.factory.annotation.Autowired

class BeforeAll extends IntegrationTest {

    @Autowired
    private LanguagesListUpdateExecutor languagesListUpdateExecutor

    def "migrations run"() {
        when:
        languagesListUpdateExecutor.run()
        println "Carmen: Languages migration executed."

        then:
        true
    }

}
