package com.cezarykluczynski.carmen

import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.cron.linguist.executor.LanguagesListUpdateExecutor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.annotations.Test

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
class BeforeAll extends AbstractTestNGSpringContextTests {

    @Autowired
    LanguagesListUpdateExecutor languagesListUpdateExecutor

    @Test
    void "should migrate"() {
        languagesListUpdateExecutor.run()
        println "Carmen: Languages migration executed."
    }

}
