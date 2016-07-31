package com.cezarykluczynski.carmen.lang.stats.mapper

import com.cezarykluczynski.carmen.cron.languages.fixture.entity.EntityOne
import com.cezarykluczynski.carmen.dao.pub.LanguagesDAO
import com.cezarykluczynski.carmen.lang.stats.domain.*
import com.cezarykluczynski.carmen.model.pub.Language as LanguageEntity
import com.google.common.collect.Lists
import com.google.common.collect.Maps
import spock.lang.Specification

class LinguistDescriptionToEntityMapperTest extends Specification {

    private static final String COMMIT_HASH = "abcdef1234abcdef1234abcdef1234abcdef1234"
    private static final String JAVA_NAME = "Java"
    private static final Long JAVA_ID = 2
    private static final Integer JAVA_ADDED = 17
    private static final Integer JAVA_REMOVED = 19
    private static final Integer JAVA_TOTAL = 101
    private static final String GROOVY_NAME = "Groovy"
    private static final Long GROOVY_ID = 3
    private static final Integer GROOVY_ADDED = 21
    private static final Integer GROOVY_REMOVED = 23
    private static final Integer GROOVY_TOTAL = 103
    private static final Long RUBY_ID = 4
    private static final String RUBY_NAME = "Ruby"

    def "updates commit entity from commit description"() {
        given:
        List languagesList = getLanguages()
        LanguagesDAO languagesDAOMock = Mock LanguagesDAO
        languagesDAOMock.findAll() >> languagesList

        EntityOne entityOne = new EntityOne()
        entityOne.language_1_added = 11
        entityOne.language_1_removed = 13
        Map<Language, LineDiffStat> lineDiffStats = Maps.newHashMap()
        lineDiffStats.put(new Language(JAVA_NAME), new LineDiffStat(JAVA_ADDED, JAVA_REMOVED))
        lineDiffStats.put(new Language(GROOVY_NAME), new LineDiffStat(GROOVY_ADDED, GROOVY_REMOVED))
        lineDiffStats.put(new Language(RUBY_NAME), null)

        CommitDescription commitDescription = new CommitDescription(COMMIT_HASH, lineDiffStats)

        LinguistDescriptionToEntityMapper linguistDescriptionToEntityMapper =
                new LinguistDescriptionToEntityMapper(languagesDAOMock)

        when:
        EntityOne resultCommit = (EntityOne) linguistDescriptionToEntityMapper
                .updateCommitUsingCommitDescription(entityOne, commitDescription)

        then:
        resultCommit.language_1_added == 0
        resultCommit.language_1_removed == 0
        resultCommit.language_2_added == JAVA_ADDED
        resultCommit.language_2_removed == JAVA_REMOVED
        resultCommit.language_3_added == GROOVY_ADDED
        resultCommit.language_3_removed == GROOVY_REMOVED
        resultCommit.language_4_added == 0
        resultCommit.language_4_removed == 0
    }

    def "updates commit entity from repository description"() {
        given:
        List languagesList = getLanguages()
        LanguagesDAO languagesDAOMock = Mock LanguagesDAO
        languagesDAOMock.findAll() >> languagesList

        EntityOne entityOne = new EntityOne()
        entityOne.language_1 = 11
        Map<Language, LineStat> lineStats = Maps.newHashMap()
        lineStats.put(new Language(JAVA_NAME), new LineStat(JAVA_TOTAL))
        lineStats.put(new Language(GROOVY_NAME), new LineStat(GROOVY_TOTAL))
        lineStats.put(new Language(RUBY_NAME), null)

        RepositoryDescription repositoryDescription = new RepositoryDescription(COMMIT_HASH, lineStats)

        LinguistDescriptionToEntityMapper linguistDescriptionToEntityMapper =
                new LinguistDescriptionToEntityMapper(languagesDAOMock)

        when:
        EntityOne resultCommit = (EntityOne) linguistDescriptionToEntityMapper
                .updateCommitUsingRepositoryDescription(entityOne, repositoryDescription)

        then:
        resultCommit.language_1 == 0
        resultCommit.language_2 == JAVA_TOTAL
        resultCommit.language_3 == GROOVY_TOTAL
        resultCommit.language_4 == 0
    }

    private static List<LanguageEntity> getLanguages() {
        LanguageEntity java = new LanguageEntity()
        java.setId JAVA_ID
        java.setName JAVA_NAME
        LanguageEntity groovy = new LanguageEntity()
        groovy.setId GROOVY_ID
        groovy.setName GROOVY_NAME
        LanguageEntity ruby = new LanguageEntity()
        ruby.setId RUBY_ID
        ruby.setName RUBY_NAME
        return Lists.newArrayList(java, groovy, ruby)
    }

}
