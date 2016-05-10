package com.cezarykluczynski.carmen.lang.stats.mapper

import com.beust.jcommander.internal.Lists
import com.beust.jcommander.internal.Maps
import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat
import com.cezarykluczynski.carmen.model.cassandra.repositories.Commit
import  com.cezarykluczynski.carmen.model.pub.Language as LanguageEntity
import org.testng.Assert
import org.testng.annotations.Test

import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

class LinguistDescriptionToEntityMapperTest {

    private static final String COMMIT_HASH = "abcdef1234abcdef1234abcdef1234abcdef1234"
    private static final String JAVA_NAME = "Java"
    private static final Long JAVA_ID = 2
    private static final Integer JAVA_ADDED = 17
    private static final Integer JAVA_REMOVED = 19
    private static final String GROOVY_NAME = "Groovy"
    private static final Long GROOVY_ID = 3
    private static final Integer GROOVY_ADDED = 21
    private static final Integer GROOVY_REMOVED = 23

    @Test
    void "updates commit entity from description"() {
        List languagesList = getLanguages()
        LanguagesDAO languagesDAOMock = mock(LanguagesDAO.class)
        when languagesDAOMock.findAll() thenReturn languagesList

        EntityOne entityOne = new EntityOne()
        entityOne.language_1_added = 11
        entityOne.language_1_removed = 13
        Map<Language, LineDiffStat> lineDiffStats = Maps.newHashMap()
        lineDiffStats.put(new Language(JAVA_NAME), new LineDiffStat(JAVA_ADDED, JAVA_REMOVED))
        lineDiffStats.put(new Language(GROOVY_NAME), new LineDiffStat(GROOVY_ADDED, GROOVY_REMOVED))

        CommitDescription commitDescription = new CommitDescription(COMMIT_HASH, lineDiffStats)

        LinguistDescriptionToEntityMapper linguistDescriptionToEntityMapper =
                new LinguistDescriptionToEntityMapper(languagesDAOMock)
        EntityOne resultCommit = (EntityOne) linguistDescriptionToEntityMapper
                .updateCommitUsingCommitDescription(entityOne, commitDescription)

        println resultCommit.language_1_added
        println resultCommit.language_1_removed
        println resultCommit.language_2_added
        println resultCommit.language_2_removed
        println resultCommit.language_3_added
        println resultCommit.language_3_removed

        Assert.assertEquals resultCommit.language_1_added, 0
        Assert.assertEquals resultCommit.language_1_removed, 0
        Assert.assertEquals resultCommit.language_2_added, JAVA_ADDED
        Assert.assertEquals resultCommit.language_2_removed, JAVA_REMOVED
        Assert.assertEquals resultCommit.language_3_added, GROOVY_ADDED
        Assert.assertEquals resultCommit.language_3_removed, GROOVY_REMOVED
    }

    private static List<LanguageEntity> getLanguages() {
        LanguageEntity java = new LanguageEntity()
        java.setId JAVA_ID
        java.setName JAVA_NAME
        LanguageEntity groovy = new LanguageEntity()
        groovy.setId GROOVY_ID
        groovy.setName GROOVY_NAME
        return Lists.newArrayList(java, groovy)
    }

}
