package com.cezarykluczynski.carmen.cron.languages.executor

import com.cezarykluczynski.carmen.cron.languages.visitor.UpdaterVisitorComposite
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Matchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify

class SchemaUpdateExecutorTest {

    UpdaterVisitorComposite updaterVisitorComposite

    SchemaUpdateExecutor schemaUpdateExecutor

    @BeforeMethod
    void setUp() {
        updaterVisitorComposite = mock UpdaterVisitorComposite.class
        schemaUpdateExecutor = new SchemaUpdateExecutor(updaterVisitorComposite)
    }

    @Test
    void "two tables"() {
        schemaUpdateExecutor.run()

        verify(updaterVisitorComposite, times(4)).visit(any())
    }

}
