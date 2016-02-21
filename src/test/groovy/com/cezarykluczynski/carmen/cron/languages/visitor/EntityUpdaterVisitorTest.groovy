package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.never
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

class EntityUpdaterVisitorTest {

    private EntityUpdaterVisitor entityUpdaterVisitor

    private RefreshableTable refreshableTable

    @BeforeMethod
    void setUp() {
        refreshableTable = mock RefreshableTable.class
        entityUpdaterVisitor = new EntityUpdaterVisitor()
    }

    @Test
    void "does not visit unchanged entity"() {
        when(refreshableTable.hasChanged()).thenReturn false

        entityUpdaterVisitor.visit refreshableTable

        verify(refreshableTable, never()).getFields()
    }

    @Test
    void "do visit changed entity"() {
        when(refreshableTable.hasChanged()).thenReturn true

        entityUpdaterVisitor.visit refreshableTable

        verify(refreshableTable).getFields()
    }

}
