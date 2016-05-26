package com.cezarykluczynski.carmen.lang.stats.persister

import com.beust.jcommander.internal.Maps
import com.cezarykluczynski.carmen.configuration.TestableApplicationConfiguration
import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
import com.cezarykluczynski.carmen.lang.stats.persistence.CassandraPersister
import com.cezarykluczynski.carmen.model.cassandra.carmen.Commit
import com.cezarykluczynski.carmen.repository.carmen.CommitsRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.springframework.test.context.web.WebAppConfiguration
import org.testng.Assert
import org.testng.annotations.AfterMethod
import org.testng.annotations.Test

@ContextConfiguration(classes = TestableApplicationConfiguration.class)
@WebAppConfiguration
public class CassandraPersisterTest extends AbstractTestNGSpringContextTests {

    private static final COMMIT_HASH = "0000000000000000000000000000000000000000"
    private static final JAVA_ADDED = 11
    private static final JAVA_REMOVED = 13
    private static final JAVA_TOTAL = 15

    @Autowired
    CommitsRepository commitsRepository

    @Autowired
    private CassandraPersister cassandraPersister

    @Test
    void "persists commit description"() {
        // setup
        Map<Language, LineDiffStat> languageLineDiffStatMap = Maps.newHashMap()
        languageLineDiffStatMap.put(new Language("Java"), new LineDiffStat(JAVA_ADDED, JAVA_REMOVED))
        CommitDescription commitDescription = new CommitDescription(COMMIT_HASH, languageLineDiffStatMap)

        // exercise
        cassandraPersister.persist commitDescription

        // assertion
        Commit commit = commitsRepository.findByHash commitDescription.commitHash
        Assert.assertNotNull commit
        Assert.assertEquals commit.language_167_added, JAVA_ADDED
        Assert.assertEquals commit.language_167_removed, JAVA_REMOVED
    }

    @Test
    void "persists repository description"() {
        // setup
        Map<Language, LineStat> languageLineStatMap = Maps.newHashMap()
        languageLineStatMap.put(new Language("Java"), new LineStat(JAVA_TOTAL))
        RepositoryDescription repositoryDescription = new RepositoryDescription(COMMIT_HASH, languageLineStatMap)

        // exercise
        cassandraPersister.persist repositoryDescription

        // assertion
        Commit commit = commitsRepository.findByHash repositoryDescription.commitHash
        Assert.assertNotNull commit
        Assert.assertEquals commit.language_167, JAVA_TOTAL
    }

    @AfterMethod
    void tearDown() {
        Commit commit = commitsRepository.findByHash COMMIT_HASH
        if (commit != null) {
            commitsRepository.delete commit
        }
    }

}
