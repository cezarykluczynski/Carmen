package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.builder.CassandraMigrationBuilder
import com.cezarykluczynski.carmen.cron.languages.model.CassandraBuiltFileNullObject
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class SchemaUpdaterVisitorTest {

    private SchemaUpdaterVisitor schemaUpdaterVisitor

    private CassandraMigrationBuilder cassandraMigrationBuilder

    private RefreshableTable refreshableTable

    @BeforeMethod
    void setUp() {
        refreshableTable = mock RefreshableTable.class
        cassandraMigrationBuilder = mock CassandraMigrationBuilder.class
        when cassandraMigrationBuilder.build(refreshableTable) thenReturn new CassandraBuiltFileNullObject()
        schemaUpdaterVisitor = new SchemaUpdaterVisitor(cassandraMigrationBuilder)
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

        verify(cassandraMigrationBuilder).build refreshableTable
    }

}
