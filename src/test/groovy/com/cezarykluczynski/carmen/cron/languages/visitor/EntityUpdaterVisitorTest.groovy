package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.builder.CassandraJavaPoetEntityBuilder
import com.cezarykluczynski.carmen.cron.languages.fixture.EntityOne
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class EntityUpdaterVisitorTest {

    private EntityUpdaterVisitor entityUpdaterVisitor

    private CassandraJavaPoetEntityBuilder cassandraJavaPoetEntityBuilder

    private RefreshableTable refreshableTable

    @BeforeMethod
    void setUp() {
        refreshableTable = mock RefreshableTable.class
        cassandraJavaPoetEntityBuilder = mock CassandraJavaPoetEntityBuilder.class
        entityUpdaterVisitor = new EntityUpdaterVisitor(cassandraJavaPoetEntityBuilder)
    }

    @Test
    void "does not visit unchanged entity"() {
        when(refreshableTable.hasChanged()).thenReturn false

        entityUpdaterVisitor.visit refreshableTable

        verify(refreshableTable, never()).getFields()
    }

    @Test
    void "do visit changed entity"() {
        when refreshableTable.hasChanged()  thenReturn true
        when refreshableTable.getBaseClass() thenReturn EntityOne.class
        when cassandraJavaPoetEntityBuilder.build(refreshableTable) thenReturn(mock(CassandraBuiltFile.class))

        entityUpdaterVisitor.visit refreshableTable

        verify(cassandraJavaPoetEntityBuilder).build refreshableTable
    }

}
