package com.cezarykluczynski.carmen.cron.languages.executor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.fixture.EntityOne
import com.cezarykluczynski.carmen.cron.languages.fixture.EntityTwo
import com.cezarykluczynski.carmen.cron.languages.visitor.UpdaterVisitorComposite
import org.apache.commons.io.FileUtils
import org.mockito.ArgumentCaptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.atLeast
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

@ContextConfiguration([
        "classpath:spring/database-config.xml",
        "classpath:spring/mvc-core-config.xml",
        "classpath:spring/cron-config.xml"
])
class SchemaUpdateExecutorTest extends AbstractTestNGSpringContextTests {

    @Autowired
    SchemaUpdateExecutor schemaUpdateExecutor

    @Test
    void "tables are refreshed"() {
        schemaUpdateExecutor.run()
    }

    @Test
    void "annotated tables are discovered"() {
        // setup
        UpdaterVisitorComposite updaterVisitorComposite = mock UpdaterVisitorComposite.class
        SchemaUpdateExecutor schemaUpdateExecutor = new SchemaUpdateExecutor(updaterVisitorComposite)

        // exercise
        schemaUpdateExecutor.run()

        // assertion
        ArgumentCaptor<RefreshableTable> argument = ArgumentCaptor.forClass RefreshableTable.class
        verify(updaterVisitorComposite, atLeast(4)).visit(argument.capture())

        Iterator<RefreshableTable> iterator = argument.getAllValues().iterator()

        int countEntityOne = 0
        int countEntityTwo = 0

        while(iterator.hasNext()) {
            Class clazz = iterator.next().getBaseClass()
            if (clazz.equals(EntityOne)) {
                countEntityOne++
            } else if (clazz.equals(EntityTwo)) {
                countEntityTwo++
            }
        }

        Assert.assertEquals countEntityOne, 2
        Assert.assertEquals countEntityTwo, 2
    }

    @AfterMethod
    void tearDown() {
        FileUtils.deleteQuietly new File("./src/main/java/com/cezarykluczynski/carmen/cron/languages/fixture/EntityOne.java")
        FileUtils.deleteQuietly new File("./src/main/java/com/cezarykluczynski/carmen/cron/languages/fixture/EntityTwo.java")
    }



}
