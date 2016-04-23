package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat;
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat;
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper;
import com.cezarykluczynski.carmen.util.network.HTTPClient;
import com.cezarykluczynski.carmen.util.network.HTTPRequestException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HTTPLangsStatsAdapter implements LangsStatsAdapter {

    private HTTPClient<JSONObject> httpClient;

    private LanguageMapper languageMapper;

    public HTTPLangsStatsAdapter(HTTPClient httpClient, LanguageMapper languageMapper) {
        this.httpClient = httpClient;
        this.languageMapper = languageMapper;
    }

    @Override
    public List<Language> getSupportedLanguages() {
        try {
            JSONObject supportedLanguages = httpClient.get("supported_languages");
            return languageMapper.mapLanguageList(supportedLanguages);
        } catch(HTTPRequestException e) {
            return null;
        }
    }

    @Override
    public String getLinguistVersion() {
        try {
            JSONObject linguistVersion = httpClient.get("linguist/version");
            System.out.println(linguistVersion);
            return languageMapper.mapLinguistVersion(linguistVersion);
        } catch(HTTPRequestException e) {
            return null;
        }
    }

    @Override
    public Map<Language, LineStat> describeRepository(String relativeDirectory, String commitHash) {
        try {
            return languageMapper.mapRepositoryDescription(httpClient.post("detector/describe_repository",
                    buildParams(relativeDirectory, commitHash)));
        } catch(HTTPRequestException e) {
            return null;
        }
    }

    @Override
    public Map<Language, LineDiffStat> describeCommit(String relativeDirectory, String commitHash) {
        try {
            Map<String, String> params = buildParams(relativeDirectory, commitHash);
            JSONObject lineDiffStats = httpClient.post("detector/describe_commit", params);
            return languageMapper.mapCommitDescription(lineDiffStats);
        } catch(HTTPRequestException e) {
            return null;
        }
    }

    private Map<String, String> buildParams(String relativeDirectory, String commitHash) {
        Map<String, String> params = new HashMap<>();
        params.put("relative_directory", relativeDirectory);
        params.put("commit_hash", commitHash);
        return params;
    }

}
