package com.cezarykluczynski.carmen.cron.languages.executor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityTwo
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import com.cezarykluczynski.carmen.cron.languages.visitor.UpdaterVisitorComposite
import org.mockito.ArgumentCaptor
import org.testng.Assert
import org.testng.annotations.Test

import static org.mockito.Mockito.atLeast
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

class SchemaUpdateExecutorTest {

    private static final LanguagesIteratorsFactory languagesIteratorFactory = new LanguagesIteratorsFactory()

    @Test
    void "annotated tables are discovered"() {
        // setup
        UpdaterVisitorComposite updaterVisitorComposite = mock UpdaterVisitorComposite.class
        SchemaUpdateExecutor schemaUpdateExecutor = new SchemaUpdateExecutor(updaterVisitorComposite, languagesIteratorFactory)

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

        Assert.assertEquals countEntityOne, 3
        Assert.assertEquals countEntityTwo, 3
    }

}
