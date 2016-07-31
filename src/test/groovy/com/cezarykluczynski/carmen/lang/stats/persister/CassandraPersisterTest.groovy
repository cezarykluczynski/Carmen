package com.cezarykluczynski.carmen.lang.stats.persister

import com.beust.jcommander.internal.Maps
import com.cezarykluczynski.carmen.IntegrationTest
import com.cezarykluczynski.carmen.lang.stats.domain.*
import com.cezarykluczynski.carmen.lang.stats.persistence.CassandraPersister
import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit
import com.cezarykluczynski.carmen.repository.carmen.CommitsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.testng.Assert

class CassandraPersisterTest extends IntegrationTest {

    private static final COMMIT_HASH = "0000000000000000000000000000000000000000"
    private static final JAVA_ADDED = 11
    private static final JAVA_REMOVED = 13
    private static final JAVA_TOTAL = 15

    @Autowired
    CommitsRepository commitsRepository

    @Autowired
    private CassandraPersister cassandraPersister

    def cleanup() {
        Commit commit = commitsRepository.findByHash COMMIT_HASH
        if (commit != null) {
            commitsRepository.delete commit
        }
    }

    def "persists commit description"() {
        given:
        Map<Language, LineDiffStat> languageLineDiffStatMap = Maps.newHashMap()
        languageLineDiffStatMap.put(new Language("Java"), new LineDiffStat(JAVA_ADDED, JAVA_REMOVED))
        CommitDescription commitDescription = new CommitDescription(COMMIT_HASH, languageLineDiffStatMap)

        when:
        cassandraPersister.persist commitDescription
        Commit commit = commitsRepository.findByHash commitDescription.commitHash

        then:
        commit != null
        commit.language_167_added == JAVA_ADDED
        commit.language_167_removed == JAVA_REMOVED
    }

    def "persists repository description"() {
        given:
        Map<Language, LineStat> languageLineStatMap = Maps.newHashMap()
        languageLineStatMap.put(new Language("Java"), new LineStat(JAVA_TOTAL))
        RepositoryDescription repositoryDescription = new RepositoryDescription(COMMIT_HASH, languageLineStatMap)

        when:
        cassandraPersister.persist repositoryDescription
        Commit commit = commitsRepository.findByHash repositoryDescription.commitHash

        then:
        Assert.assertNotNull commit
        Assert.assertEquals commit.language_167, JAVA_TOTAL
    }

}
