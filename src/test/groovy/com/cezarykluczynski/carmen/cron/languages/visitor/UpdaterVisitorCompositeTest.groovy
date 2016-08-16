package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import spock.lang.Specification

class UpdaterVisitorCompositeTest extends Specification {

    private EntityUpdaterVisitor entityUpdaterVisitor

    private LanguagesDiffStatisticsUpdaterVisitor languagesDiffStatisticsUpdaterVisitor

    private LanguagesStatisticsUpdaterVisitor languagesStatisticsUpdaterVisitor

    private SchemaUpdaterVisitor schemaUpdaterVisitor

    private UpdaterVisitorComposite updaterVisitorComposite

    private LanguagesDAO languagesDAO

    private RefreshableTable refreshableTable

    def setup() {
        refreshableTable = Mock RefreshableTable
        languagesDAO = Mock LanguagesDAO
        entityUpdaterVisitor = Mock EntityUpdaterVisitor
        languagesDiffStatisticsUpdaterVisitor = Mock LanguagesDiffStatisticsUpdaterVisitor
        languagesStatisticsUpdaterVisitor = Mock LanguagesStatisticsUpdaterVisitor
        schemaUpdaterVisitor = Mock SchemaUpdaterVisitor
        updaterVisitorComposite = new UpdaterVisitorComposite(entityUpdaterVisitor,
                languagesDiffStatisticsUpdaterVisitor, languagesStatisticsUpdaterVisitor, schemaUpdaterVisitor)
    }

    def "all components are called"() {
        when:
        updaterVisitorComposite.visit refreshableTable

        then:
        1 * entityUpdaterVisitor.visit(refreshableTable)
        1 * languagesDiffStatisticsUpdaterVisitor.visit(refreshableTable)
        1 * languagesStatisticsUpdaterVisitor.visit(refreshableTable)
        1 * schemaUpdaterVisitor.visit(refreshableTable)
    }

}
