package com.cezarykluczynski.carmen.lang.stats.adapter;

import com.cezarykluczynski.carmen.lang.stats.domain.*;
import com.cezarykluczynski.carmen.lang.stats.mapper.LanguageMapper;
import com.cezarykluczynski.carmen.util.exec.Executor;
import com.cezarykluczynski.carmen.util.exec.LanguageStatsCommand;
import com.cezarykluczynski.carmen.util.exec.Result;
import org.json.JSONObject;

import java.util.List;

public class CLILangsStatsAdapter implements LangsStatsAdapter {

    private String binPath = "./ruby/bin/lang_stats";

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
    public String getLinguistVersion() {
        Result commandResult = Executor.execute(LanguageStatsCommand.createLinguistVersionCommand(binPath));
        if (commandResult.isSuccessFul()) {
            return languageMapper.mapLinguistVersion(new JSONObject(commandResult.getOutput()));
        }

        return null;
    }

    @Override
    public RepositoryDescription describeRepository(String relativeDirectory, String commitHash) {
        Result commandResult = Executor.execute(
                LanguageStatsCommand.createDescribeRepositoryCommand(binPath, relativeDirectory, commitHash));

        if (commandResult.isSuccessFul()) {
            return languageMapper.toRepositoryDescription(commitHash, new JSONObject(commandResult.getOutput()));
        }

        return null;
    }

    @Override
    public CommitDescription describeCommit(String relativeDirectory, String commitHash) {
        Result commandResult = Executor.execute(
                LanguageStatsCommand.createDescribeCommitCommand(binPath, relativeDirectory, commitHash));

        if (commandResult.isSuccessFul()) {
            return languageMapper.toCommitDescription(commitHash, new JSONObject(commandResult.getOutput()));
        }

        return null;
    }
}
