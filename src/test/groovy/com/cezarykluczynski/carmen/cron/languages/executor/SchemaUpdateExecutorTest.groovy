package com.cezarykluczynski.carmen.cron.languages.executor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityTwo
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import com.cezarykluczynski.carmen.cron.languages.visitor.UpdaterVisitorComposite
import spock.lang.Specification

class SchemaUpdateExecutorTest extends Specification {

    private LanguagesIteratorsFactory languagesIteratorFactory

    def setup() {
        languagesIteratorFactory = new LanguagesIteratorsFactory()
    }

    def "annotated tables are discovered"() {
        given:
        int countEntityOne = 0
        int countEntityTwo = 0
        UpdaterVisitorComposite updaterVisitorComposite = Mock UpdaterVisitorComposite
        SchemaUpdateExecutor schemaUpdateExecutor = new SchemaUpdateExecutor(updaterVisitorComposite, languagesIteratorFactory)

        when:
        schemaUpdateExecutor.run()

        then:
        4 * updaterVisitorComposite.visit(_) >> { args ->
            RefreshableTable refreshableTable = (RefreshableTable) args[0]
            Class clazz = refreshableTable.getBaseClass()

            if (clazz.equals(EntityOne)) {
                countEntityOne++
            } else if (clazz.equals(EntityTwo)) {
                countEntityTwo++
            }
        }
        countEntityOne == 1
        countEntityTwo == 1
    }

}
