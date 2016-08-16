package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.CassandraBuiltFile
import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.builder.CassandraJavaPoetEntityBuilder
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import spock.lang.Specification

class EntityUpdaterVisitorTest extends Specification {

    private EntityUpdaterVisitor entityUpdaterVisitor

    private CassandraJavaPoetEntityBuilder cassandraJavaPoetEntityBuilder

    private RefreshableTable refreshableTable

    def setup() {
        refreshableTable = Mock RefreshableTable
        cassandraJavaPoetEntityBuilder = Mock CassandraJavaPoetEntityBuilder
        entityUpdaterVisitor = new EntityUpdaterVisitor(cassandraJavaPoetEntityBuilder)
    }

    def "does not visit unchanged entity"() {
        given:
        refreshableTable.hasChanged() >> false

        when:
        entityUpdaterVisitor.visit refreshableTable

        then:
        0 * refreshableTable.getFields()
    }

    def "do visit changed entity"() {
        given:
        refreshableTable.hasChanged() >> true
        refreshableTable.getBaseClass() >> EntityOne.class

        when:
        entityUpdaterVisitor.visit refreshableTable

        then:
        1 * cassandraJavaPoetEntityBuilder.build(refreshableTable) >> Mock(CassandraBuiltFile)
    }

}
