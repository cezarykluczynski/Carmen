package com.cezarykluczynski.carmen.cron.languages.model

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor
import com.cezarykluczynski.carmen.cron.languages.fixture.EntityOne
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class RefreshableTableImplTest {

    private RefreshableTable refreshableTable

    private final String ONE_MORE = "one_more"

    @BeforeMethod
    void setUp() {
        refreshableTable = new RefreshableTableImpl(EntityOne)
    }

    @Test
    void hasChangedAndFinalizeFieldsCount() {
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
