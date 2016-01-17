package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineAction;
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper;
import com.cezarykluczynski.carmen.util.network.HTTPClient;
import com.cezarykluczynski.carmen.util.network.HTTPRequestException;
import org.json.JSONObject;

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
            return languageMapper.mapList(supportedLanguages);
        } catch(HTTPRequestException e) {
            return null;
        }
    }

    @Override
    public Map<Language, Integer> describeRepository(String relativeDirectory, String commitHash) {
        return null;
    }

    @Override
    public Map<Language, Map<LineAction, Integer>> describeCommit(String relativeDirectory, String commitHash) {
        return null;
    }

}
