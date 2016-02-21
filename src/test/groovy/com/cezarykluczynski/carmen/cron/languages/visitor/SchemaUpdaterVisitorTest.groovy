package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class SchemaUpdaterVisitorTest {

    private SchemaUpdaterVisitor schemaUpdaterVisitor

    private RefreshableTable refreshableTable

    @BeforeMethod
    void setUp() {
        refreshableTable = mock RefreshableTable.class
        schemaUpdaterVisitor = new SchemaUpdaterVisitor()
    }

    @Test
    void "does not visit unchanged entity"() {
        when(refreshableTable.hasChanged()).thenReturn false

        schemaUpdaterVisitor.visit refreshableTable

        verify(refreshableTable, never()).getFields()
    }

    @Test
    void "do visit changed entity"() {
        when(refreshableTable.hasChanged()).thenReturn true

        schemaUpdaterVisitor.visit refreshableTable

        verify(refreshableTable).getFields()
    }

}
