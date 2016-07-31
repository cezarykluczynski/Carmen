package com.cezarykluczynski.carmen.lang.stats.adapter

import com.cezarykluczynski.carmen.lang.stats.domain.CommitDescription
import com.cezarykluczynski.carmen.lang.stats.domain.Language
import com.cezarykluczynski.carmen.lang.stats.domain.RepositoryDescription
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper
import com.cezarykluczynski.carmen.lang.stats.mapper.LinguistLanguageMapper
import com.cezarykluczynski.carmen.lang.stats.util.LanguageDetectorServerSwitcher
import com.cezarykluczynski.carmen.util.network.HTTPClient
import com.cezarykluczynski.carmen.util.network.HTTPJSONClientImpl
import com.cezarykluczynski.carmen.util.network.HTTPRequestException
import org.testng.Assert
import spock.lang.Requires
import spock.lang.Shared
import spock.lang.Specification

@Requires({ LanguageDetectorServerSwitcher.getAssumeRunningServer() ||
        LanguageDetectorServerSwitcher.getClient() == 'http' })
class HTTPLangsStatsAdapterIntegrationTest extends Specification {

    @Shared
    private String detectorIp

    @Shared
    private int detectorPort

    private LanguageMapper languageMapper = new LinguistLanguageMapper()

    private HTTPLangsStatsAdapter httpLangsStatsAdapter

    private HTTPClient httpClient

    private HTTPClient invalidHttpClientMock

    def setupSpec() {
        Properties prop = LanguageDetectorServerSwitcher.getApplicationProperties()
        detectorIp = prop.getProperty('detector.ip')
        detectorPort = Integer.valueOf prop.getProperty('detector.port')
    }

    def setup() {
        httpClient = new HTTPJSONClientImpl(detectorIp, detectorPort)

        invalidHttpClientMock = Mock HTTPJSONClientImpl
        invalidHttpClientMock.get(_) >> { args ->
            throw new HTTPRequestException(new Throwable())
        }
        invalidHttpClientMock.post(*_) >> { args ->
            throw new HTTPRequestException(new Throwable())
        }

        httpLangsStatsAdapter = new HTTPLangsStatsAdapter(httpClient, languageMapper)
    }

    def "gets supported languages"() {
        given:
        injectValidHttpClient()

        when:
        List<Language> languageList = httpLangsStatsAdapter.getSupportedLanguages()

        then:
        Assert.assertEquals languageList.size(), 401
    }

    def "returns null for unsuccessful supported languages request"() {
        given:
        injectInvalidHttpClient()

        expect:
        httpLangsStatsAdapter.getSupportedLanguages() == null
    }

    def "gets linguist version"() {
        given:
        injectValidHttpClient()

        when:
        String linguistVersion = httpLangsStatsAdapter.getLinguistVersion()

        then:
        linguistVersion.matches("\\d\\.\\d\\.\\d")
    }

    def "returns null for unsuccessful linguist version request"() {
        given:
        injectInvalidHttpClient()

        expect:
        httpLangsStatsAdapter.getLinguistVersion() == null
    }

    def "describes repository"() {
        given:
        injectValidHttpClient()
        final String commitHash = "3fe8afa350b369c6c697290f64da6aa996ede153"

        when:
        RepositoryDescription repositoryDescription = httpLangsStatsAdapter.describeRepository(".", commitHash)

        then:
        repositoryDescription.getLineStats().size() == 4
        repositoryDescription.getCommitHash() == commitHash
    }

    def "returns null for unsuccessful repository description request"() {
        given:
        injectInvalidHttpClient()

        expect:
        httpLangsStatsAdapter.describeRepository(".", "3fe8afa350b369c6c697290f64da6aa996ede153") == null
    }

    def "describes commit"() {
        given:
        injectValidHttpClient()
        final String commitHash = "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1"

        when:
        CommitDescription commitDescription =
                httpLangsStatsAdapter.describeCommit(".", commitHash)

        then:
        commitDescription.getLineDiffStats().size() == 2
        commitDescription.getCommitHash() == commitHash
    }

    def "returns null for unsuccessful commit description request"() {
        given:
        injectInvalidHttpClient()

        expect:
        httpLangsStatsAdapter.describeCommit(".", "21628ec99e149f6509bfb3b3ce8faf8eb2f391c1") == null
    }

    private void injectValidHttpClient() {
        injectHttpClient httpClient
    }

    private void injectInvalidHttpClient() {
        injectHttpClient invalidHttpClientMock
    }

    private void injectHttpClient(HTTPClient httpClient) {
        httpLangsStatsAdapter.httpClient = httpClient
    }

}
