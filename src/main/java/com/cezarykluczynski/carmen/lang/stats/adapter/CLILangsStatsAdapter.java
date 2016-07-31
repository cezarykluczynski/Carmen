package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.*;
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper;
import com.cezarykluczynski.carmen.util.exec.executor.Executor;
import com.cezarykluczynski.carmen.util.exec.LanguageStatsCommand;
import com.cezarykluczynski.carmen.util.exec.result.Result;
import org.json.JSONObject;

import java.util.List;

public class CLILangsStatsAdapter implements LangsStatsAdapter {

    private String binCommandPrefix = "ruby ./ruby/bin/lang_stats";

    private LanguageMapper languageMapper;

    public CLILangsStatsAdapter(LanguageMapper languageMapper) {
        this.languageMapper = languageMapper;
    }

    @Override
    public List<Language> getSupportedLanguages() {
        Result commandResult = Executor.execute(LanguageStatsCommand.createSupportedLanguagesCommand(binCommandPrefix));
        if (commandResult.isSuccessFul()) {
            return languageMapper.mapLanguageList(new JSONObject(commandResult.getOutput()));
        }

        return null;
    }

    @Override
    public String getLinguistVersion() {
        Result commandResult = Executor.execute(LanguageStatsCommand.createLinguistVersionCommand(binCommandPrefix));
        if (commandResult.isSuccessFul()) {
            return languageMapper.mapLinguistVersion(new JSONObject(commandResult.getOutput()));
        }

        return null;
    }

    @Override
    public RepositoryDescription describeRepository(String relativeDirectory, String commitHash) {
        Result commandResult = Executor.execute(
                LanguageStatsCommand.createDescribeRepositoryCommand(binCommandPrefix, relativeDirectory, commitHash));

        if (commandResult.isSuccessFul()) {
            return languageMapper.toRepositoryDescription(commitHash, new JSONObject(commandResult.getOutput()));
        }

        return null;
    }

    @Override
    public CommitDescription describeCommit(String relativeDirectory, String commitHash) {
        Result commandResult = Executor.execute(
                LanguageStatsCommand.createDescribeCommitCommand(binCommandPrefix, relativeDirectory, commitHash));

        if (commandResult.isSuccessFul()) {
            return languageMapper.toCommitDescription(commitHash, new JSONObject(commandResult.getOutput()));
        }

        return null;
    }
}
