package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.cron.languages.factory.TreeSetEntityFieldFactory
import com.cezarykluczynski.carmen.cron.languages.model.EntityField
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.model.pub.Language
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class LanguagesStatisticsUpdaterVisitorTest {

    private LanguagesStatisticsUpdaterVisitor languagesStatisticsUpdaterVisitor

    private LanguagesDAO languagesDAO

    private RefreshableTable refreshableTable

    @BeforeMethod
    void setUp() {
        refreshableTable = mock RefreshableTable.class
        languagesDAO = mock LanguagesDAO.class
        languagesStatisticsUpdaterVisitor = new LanguagesStatisticsUpdaterVisitor(languagesDAO)
    }

    @Test
    void "field list is updated"() {
        SortedSet<EntityField> fieldsSet = TreeSetEntityFieldFactory.create()
        fieldsSet.add new EntityField("language_1")
        fieldsSet.add new EntityField("language_2")
        when refreshableTable.getFields() thenReturn fieldsSet
        List<Language> languageList = new ArrayList<>()
        Language language3 = new Language()
        Language language4 = new Language()
        language3.setId 3
        language4.setId 4
        languageList.add language3
        languageList.add language4
        when languagesDAO.findAll() thenReturn languageList

        languagesStatisticsUpdaterVisitor.visit refreshableTable

        SortedSet<String> fields = refreshableTable.getFields()

        Assert.assertEquals fields.size(), 4
        Assert.assertTrue fields.contains(new EntityField("language_3"))
        Assert.assertTrue fields.contains(new EntityField("language_4"))
    }

}
