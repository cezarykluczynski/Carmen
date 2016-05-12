package com.cezarykluczynski.carmen.cron.languages.model

import com.cezarykluczynski.carmen.cron.languages.api.FieldsFilter
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.cron.languages.iterator.LanguagesIteratorsFactory
import org.apache.commons.lang.math.RandomUtils
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import java.util.stream.Collectors

class RefreshableTableImplTest {

    private RefreshableTable refreshableTable

    private final String ONE_MORE = "one_more"

    private static final LanguagesIteratorsFactory languagesIteratorFactory = new LanguagesIteratorsFactory()

    @BeforeMethod
    void setUp() {
        refreshableTable = new RefreshableTableImpl(EntityOne,
                languagesIteratorFactory.createEntityFieldsIterator(EntityOne, FieldsFilter.ALL))
    }

    @Test
    void "has changed when fields was changed"() {
        Assert.assertFalse refreshableTable.hasChanged()

        refreshableTable.accept(new RefreshableTableVisitor() {
            @Override
            void visit(RefreshableTable refreshableTable) {
                SortedSet<EntityField> fields = refreshableTable.getFields()
                fields.add new EntityField(ONE_MORE)
                refreshableTable.setFields fields
            }
        })

        Assert.assertTrue refreshableTable.hasChanged()
    }

    @Test
    void "has not changed when fields were not changed"() {
        Assert.assertFalse refreshableTable.hasChanged()

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
        Assert.assertFalse refreshableTable.hasChanged()
    }

    @Test
    void gettersAndSetters() {
        Assert.assertEquals refreshableTable.getBaseClass(), EntityOne.class

        SortedSet<EntityField> fields = refreshableTable.getFields()

        Assert.assertEquals fields.size(), 10

        fields.add new EntityField(ONE_MORE)

        Assert.assertEquals refreshableTable.getFields().size(), 10

        refreshableTable.setFields fields

        Assert.assertEquals refreshableTable.getFields().size(), 11
        Assert.assertEquals refreshableTable.getNewFields().size(), 1
        Assert.assertEquals refreshableTable.getNewFields().first(), new EntityField(ONE_MORE)
    }

}
