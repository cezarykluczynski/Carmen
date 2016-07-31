package com.cezarykluczynski.carmen.lang.stats.adapter

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
import com.cezarykluczynski.carmen.lang.stats.mapper.LinguistLanguageMapper
import com.cezarykluczynski.carmen.lang.stats.util.LanguageDetectorServerSwitcher
import org.testng.Assert
import spock.lang.Requires
import spock.lang.Specification

@Requires({ LanguageDetectorServerSwitcher.getClient() == 'cli' })
public class CLILangsStatsAdapterIntegrationTest extends Specification {

    private CLILangsStatsAdapter cliLangsStatsAdapter

    def setup() {
        cliLangsStatsAdapter = new CLILangsStatsAdapter(new LinguistLanguageMapper())
    }

    def "gets supported languages"() {
        given:
        injectValidBinPath()

        when:
        List<Language> languageList = cliLangsStatsAdapter.getSupportedLanguages()

        then:
        Assert.assertEquals languageList.size(), 401
    }

    def "returns null for unsuccessful supported languages request"() {
        given:
        injectInvalidBinPath()

        expect:
        cliLangsStatsAdapter.getSupportedLanguages() == null
    }

    def "gets linguist version"() {
        given:
        injectValidBinPath()

        when:
        String linguistVersion = cliLangsStatsAdapter.getLinguistVersion()

        then:
        linguistVersion.matches("\\d\\.\\d\\.\\d")
    }

    def "returns null for unsuccessful linguist version request"() {
        given:
        injectInvalidBinPath()

        expect:
        cliLangsStatsAdapter.getLinguistVersion() == null
    }

    def "describes repository"() {
        given:
        injectValidBinPath()
        final String commitHash = "3fe8afa350b369c6c697290f64da6aa996ede153"

        when:
        RepositoryDescription repositoryDescription =
                cliLangsStatsAdapter.describeRepository(".", commitHash)

        then:
        repositoryDescription.getLineStats().size() == 4
        repositoryDescription.getCommitHash() == commitHash
    }

    def "returns null for unsuccessful repository description request"() {
        given:
        injectInvalidBinPath()

        expect:
        cliLangsStatsAdapter.describeRepository(".", "3fe8afa350b369c6c697290f64da6aa996ede153") == null
    }

    def "describes commit"() {
        given:
        injectValidBinPath()
        final String commitHash = "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1"

        when:
        CommitDescription commitDescription =
                cliLangsStatsAdapter.describeCommit(".", commitHash)

        then:
        commitDescription.getLineDiffStats().size() == 2
        commitDescription.getCommitHash() == commitHash
    }

    def "returns null for unsuccessful commit description request"() {
        given:
        injectInvalidBinPath()

        expect:
        cliLangsStatsAdapter.describeCommit(".", "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1") == null
    }

    private void injectInvalidBinPath() {
        injectBinPath "ruby ./invalid/bin/path"
    }

    private void injectValidBinPath() {
        injectBinPath "ruby ./ruby/bin/lang_stats"
    }

    private injectBinPath(String binPath) {
        cliLangsStatsAdapter.binCommandPrefix = binPath
    }

}
