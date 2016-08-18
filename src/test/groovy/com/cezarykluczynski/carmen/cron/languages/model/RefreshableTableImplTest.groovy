package com.cezarykluczynski.carmen.cron.languages.model

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import spock.lang.Specification

class RefreshableTableImplTest extends Specification {

    private RefreshableTable refreshableTable

    private final String ONE_MORE = "one_more"

    private static final LanguagesIteratorsFactory languagesIteratorFactory = new LanguagesIteratorsFactory()

    def setup() {
        refreshableTable = new RefreshableTableImpl(EntityOne,
                languagesIteratorFactory.createEntityFieldsIterator(EntityOne, FieldsFilter.ALL))
    }

    def "does not initialize in changed state"() {
        expect:
        !refreshableTable.hasChanged()
    }

    def "has changed when fields was changed"() {
        when:
        refreshableTable.accept(new RefreshableTableVisitor() {
            @Override
            void visit(RefreshableTable refreshableTable) {
                SortedSet<EntityField> fields = refreshableTable.getFields()
                fields.add new EntityField(ONE_MORE)
                refreshableTable.setFields fields
            }
        })

        then:
        refreshableTable.hasChanged()
    }

    def "has not changed when fields were not changed"() {
        when:
        refreshableTable.accept(new RefreshableTableVisitor() {
            @Override
            void visit(RefreshableTable refreshableTable) {
                SortedSet<EntityField> fields = TreeSetEntityFieldFactory.create()

                Iterator<EntityField> entityFieldIterator = refreshableTable.getFields().iterator()
                while (entityFieldIterator.hasNext()) {
                    EntityField entityField = entityFieldIterator.next()
                    fields.add(new EntityField(entityField.getName(), entityField.getType()))
                }

                refreshableTable.setFields fields
            }
        })

        then:
        !refreshableTable.hasChanged()
    }

    def "getters and setters works properly"() {
        given:
        SortedSet<EntityField> fields = refreshableTable.getFields()
        assert fields.size() == 13
        fields.add new EntityField(ONE_MORE)


        when:
        refreshableTable.setFields fields

        then:
        refreshableTable.getBaseClass() == EntityOne.class
        refreshableTable.getFields().size() == 14
        refreshableTable.getNewFields().size() == 1
        refreshableTable.getNewFields().first() == new EntityField(ONE_MORE)
    }

}
