package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.verify

class UpdaterVisitorCompositeTest {

    private EntityUpdaterVisitor entityUpdaterVisitor

    private LanguagesDiffStatisticsUpdaterVisitor languagesDiffStatisticsUpdaterVisitor

    private LanguagesStatisticsUpdaterVisitor languagesStatisticsUpdaterVisitor

    private SchemaUpdaterVisitor schemaUpdaterVisitor

    private UpdaterVisitorComposite updaterVisitorComposite

    private LanguagesDAO languagesDAO

    private RefreshableTable refreshableTable

    @BeforeMethod
    void setUp() {
        refreshableTable = mock RefreshableTable.class
        languagesDAO = mock LanguagesDAO.class
        entityUpdaterVisitor = mock EntityUpdaterVisitor.class
        languagesDiffStatisticsUpdaterVisitor = mock LanguagesDiffStatisticsUpdaterVisitor.class
        languagesStatisticsUpdaterVisitor = mock LanguagesStatisticsUpdaterVisitor.class
        schemaUpdaterVisitor = mock SchemaUpdaterVisitor.class
        updaterVisitorComposite = new UpdaterVisitorComposite(entityUpdaterVisitor,
                languagesDiffStatisticsUpdaterVisitor, languagesStatisticsUpdaterVisitor, schemaUpdaterVisitor)
    }

    @Test
    void "all components are called"() {
        updaterVisitorComposite.visit refreshableTable

        verify entityUpdaterVisitor visit(refreshableTable)
        verify languagesDiffStatisticsUpdaterVisitor visit(refreshableTable)
        verify languagesStatisticsUpdaterVisitor visit(refreshableTable)
        verify schemaUpdaterVisitor visit(refreshableTable)
    }

}
