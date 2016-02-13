package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.Language;
import com.cezarykluczynski.carmen.lang.stats.domain.LineDiffStat;
import com.cezarykluczynski.carmen.lang.stats.domain.LineStat;
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper;
import com.cezarykluczynski.carmen.util.exec.Executor;
import com.cezarykluczynski.carmen.util.exec.LanguageStatsCommand;
import com.cezarykluczynski.carmen.util.exec.Result;
import org.json.JSONObject;

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
        Result commandResult = Executor.execute(LanguageStatsCommand.createSupportedLanguagesCommand(binPath));
        if (commandResult.isSuccessFul()) {
            return languageMapper.mapLanguageList(new JSONObject(commandResult.getOutput()));
        }

        return null;
    }

    @Override
    public Map<Language, LineStat> describeRepository(String relativeDirectory, String commitHash) {
        Result commandResult = Executor.execute(
                LanguageStatsCommand.createDescribeRepositoryCommand(binPath, relativeDirectory, commitHash));

        if (commandResult.isSuccessFul()) {
            return languageMapper.mapRepositoryDescription(new JSONObject(commandResult.getOutput()));
        }

        return null;
    }

    @Override
    public Map<Language, LineDiffStat> describeCommit(String relativeDirectory, String commitHash) {
        Result commandResult = Executor.execute(
                LanguageStatsCommand.createDescribeCommitCommand(binPath, relativeDirectory, commitHash));

        if (commandResult.isSuccessFul()) {
            return languageMapper.mapCommitDescription(new JSONObject(commandResult.getOutput()));
        }

        return null;
    }
}
