package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import com.cezarykluczynski.carmen.data.language.model.entity.Language
import com.cezarykluczynski.carmen.data.language.model.repository.LanguageRepository
import spock.lang.Specification

class LanguagesDiffStatisticsUpdaterVisitorTest extends Specification {

    private LanguagesDiffStatisticsUpdaterVisitor languagesDiffStatisticsUpdaterVisitor

    private LanguageRepository languageRepository

    private RefreshableTable refreshableTable

    def setup() {
        refreshableTable = Mock RefreshableTable
        languageRepository = Mock LanguageRepository
        languagesDiffStatisticsUpdaterVisitor = new LanguagesDiffStatisticsUpdaterVisitor(languageRepository)
    }

    def "field list is updated"() {
        given:
        SortedSet<EntityField> fieldsSet = TreeSetEntityFieldFactory.create()
        fieldsSet.add new EntityField("language_1_added")
        fieldsSet.add new EntityField("language_1_removed")
        fieldsSet.add new EntityField("language_2_added")
        fieldsSet.add new EntityField("language_2_removed")
        refreshableTable.getFields() >> fieldsSet
        List<Language> languageList = new ArrayList<>()
        Language language3 = new Language()
        Language language4 = new Language()
        language3.setId 3
        language4.setId 4
        languageList.add language3
        languageList.add language4
        languageRepository.findAll() >> languageList

        when:
        languagesDiffStatisticsUpdaterVisitor.visit refreshableTable
        SortedSet<EntityField> fields = refreshableTable.getFields()

        then:
        fields.size() == 8
        fields.contains(new EntityField("language_3_added"))
        fields.contains(new EntityField("language_3_removed"))
        fields.contains(new EntityField("language_4_added"))
        fields.contains(new EntityField("language_4_removed"))
    }

}
