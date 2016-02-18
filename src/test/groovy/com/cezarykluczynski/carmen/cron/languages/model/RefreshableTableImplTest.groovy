package com.cezarykluczynski.carmen.cron.languages.model

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTableVisitor
import com.cezarykluczynski.carmen.cron.languages.iterator.fixture.Entity1
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

class RefreshableTableImplTest {

    private RefreshableTable refreshableTable

    private final String ONE_MORE = "one_more"

    @BeforeMethod
    void setUp() {
        refreshableTable = new RefreshableTableImpl(Entity1)
    }

    @Test
    void hasChangedAndFinalizeFieldsCount() {
        Assert.assertFalse refreshableTable.hasChanged()

        refreshableTable.finalizeFieldsCount()

        Assert.assertFalse refreshableTable.hasChanged()

        refreshableTable.accept(new RefreshableTableVisitor() {
            @Override
            void visit(RefreshableTable refreshableTable) {
                SortedSet<String> fields = refreshableTable.getFields()
                fields.add ONE_MORE
                refreshableTable.setFields fields
            }
        })

        Assert.assertFalse refreshableTable.hasChanged()

        refreshableTable.finalizeFieldsCount()

        Assert.assertTrue refreshableTable.hasChanged()
    }

    @Test
    void gettersAndSetters() {
        Assert.assertEquals refreshableTable.getBaseClass(), Entity1.class

        SortedSet<String> fields = refreshableTable.getFields()

        Assert.assertEquals fields.size(), 9

        fields.add ONE_MORE

        Assert.assertEquals refreshableTable.getFields().size(), 9

        refreshableTable.setFields fields

        Assert.assertEquals refreshableTable.getFields().size(), 10
    }

}
