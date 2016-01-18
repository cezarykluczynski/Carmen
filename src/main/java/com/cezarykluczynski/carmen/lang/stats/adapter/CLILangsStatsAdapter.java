package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat;
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat;
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper;

import java.util.List;
import java.util.Map;

public class CLILangsStatsAdapter implements LangsStatsAdapter {

    private final String binPath = "./ruby/bin/lang_stats";

    private LanguageMapper languageMapper;

    public CLILangsStatsAdapter(LanguageMapper languageMapper) {
        this.languageMapper = languageMapper;
    }

    @Override
    public List<Language> getSupportedLanguages() {
        return null;
    }

    @Override
    public Map<Language, LineStat> describeRepository(String relativeDirectory, String commitHash) {
        return null;
    }

    @Override
    public Map<Language, LineDiffStat> describeCommit(String relativeDirectory, String commitHash) {
        return null;
    }
}
