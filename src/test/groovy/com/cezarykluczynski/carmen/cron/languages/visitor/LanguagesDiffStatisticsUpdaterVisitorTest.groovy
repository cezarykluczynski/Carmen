package com.cezarykluczynski.carmen.cron.languages.visitor

import com.cezarykluczynski.carmen.cron.languages.api.RefreshableTable
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.model.pub.Language
import org.testng.Assert
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class LanguagesDiffStatisticsUpdaterVisitorTest {

    private LanguagesDiffStatisticsUpdaterVisitor languagesDiffStatisticsUpdaterVisitor

    private LanguagesDAO languagesDAO

    private RefreshableTable refreshableTable

    @BeforeMethod
    void setUp() {
        refreshableTable = mock RefreshableTable.class
        languagesDAO = mock LanguagesDAO.class
        languagesDiffStatisticsUpdaterVisitor = new LanguagesDiffStatisticsUpdaterVisitor(languagesDAO)
    }

    @Test
    void "field list is updated"() {
        SortedSet<String> fieldsSet = new TreeSet<String>()
        fieldsSet.add "language_1_added"
        fieldsSet.add "language_1_removed"
        fieldsSet.add "language_2_added"
        fieldsSet.add "language_2_removed"
        when refreshableTable.getFields() thenReturn fieldsSet
        List<Language> languageList = new ArrayList<>()
        Language language3 = new Language()
        Language language4 = new Language()
        language3.setId 3
        language4.setId 4
        languageList.add language3
        languageList.add language4
        when languagesDAO.findAll() thenReturn languageList

        languagesDiffStatisticsUpdaterVisitor.visit refreshableTable

        SortedSet<String> fields = refreshableTable.getFields()

        Assert.assertEquals fields.size(), 8
        Assert.assertTrue fields.contains("language_3_added")
        Assert.assertTrue fields.contains("language_3_removed")
        Assert.assertTrue fields.contains("language_4_added")
        Assert.assertTrue fields.contains("language_4_removed")
    }

}
