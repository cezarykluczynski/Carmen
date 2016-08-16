package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.builder.CassandraMigrationBuilder
import com.cezarykluczynski.carmen.cron.languages.model.CassandraBuiltFileNullObject
import spock.lang.Specification

class SchemaUpdaterVisitorTest extends Specification {

    private SchemaUpdaterVisitor schemaUpdaterVisitor

    private CassandraMigrationBuilder cassandraMigrationBuilder

    private RefreshableTable refreshableTable

    def setup() {
        refreshableTable = Mock RefreshableTable
        cassandraMigrationBuilder = Mock CassandraMigrationBuilder
        schemaUpdaterVisitor = new SchemaUpdaterVisitor(cassandraMigrationBuilder)
    }

    def "does not visit unchanged entity"() {
        when:
        schemaUpdaterVisitor.visit refreshableTable

        then:
        1 * refreshableTable.hasChanged() >> false
        0 * refreshableTable.getFields()
        0 * cassandraMigrationBuilder.build(refreshableTable)
    }

    def "do visit changed entity"() {
        when:
        schemaUpdaterVisitor.visit refreshableTable

        then:
        1 * refreshableTable.hasChanged() >> true
        1 * cassandraMigrationBuilder.build(refreshableTable) >> new CassandraBuiltFileNullObject()
    }

}
