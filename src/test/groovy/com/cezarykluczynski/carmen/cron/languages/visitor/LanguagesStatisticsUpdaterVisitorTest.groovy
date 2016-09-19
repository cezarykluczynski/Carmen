package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import com.cezarykluczynski.carmen.data.language.model.entity.Language
import com.cezarykluczynski.carmen.data.language.model.repository.LanguageRepository
import com.google.common.collect.Lists
import spock.lang.Specification

class LanguagesStatisticsUpdaterVisitorTest extends Specification {

    private LanguagesStatisticsUpdaterVisitor languagesStatisticsUpdaterVisitor

    private LanguageRepository languageRepository

    private RefreshableTable refreshableTable

    def setup() {
        refreshableTable = Mock RefreshableTable
        languageRepository = Mock LanguageRepository
        languagesStatisticsUpdaterVisitor = new LanguagesStatisticsUpdaterVisitor(languageRepository)
    }

    def "field list is updated"() {
        given:
        SortedSet<EntityField> fieldsSet = TreeSetEntityFieldFactory.create()
        fieldsSet.add new EntityField("language_1")
        fieldsSet.add new EntityField("language_2")
        refreshableTable.getFields() >> fieldsSet
        List<Language> languageList = Lists.newArrayList()
        Language language3 = new Language()
        Language language4 = new Language()
        language3.setId 3
        language4.setId 4
        languageList.add language3
        languageList.add language4
        languageRepository.findAll() >> languageList

        when:
        languagesStatisticsUpdaterVisitor.visit refreshableTable
        SortedSet<EntityField> fields = refreshableTable.getFields()

        then:
        fields.size() == 4
        fields.contains(new EntityField("language_3"))
        fields.contains(new EntityField("language_4"))
    }

}
