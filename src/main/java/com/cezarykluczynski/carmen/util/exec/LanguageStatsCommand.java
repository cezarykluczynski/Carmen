package com.cezarykluczynski.carmen.util.exec;

import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;
import com.cezarykluczynski.carmen.util.exec.command.Command;

public class LanguageStatsCommand extends ApacheCommonsCommand {

    public static Command createSupportedLanguagesCommand(String binPath) {
        return new LanguageStatsCommand(binPath + " supported_languages");
    }

    public static Command createLinguistVersionCommand(String binPath) {
        return new LanguageStatsCommand(binPath + " linguist_version");
    }

    public static Command createDescribeRepositoryCommand(String binPath, String relativeDirectory, String commitHash) {
        return new LanguageStatsCommand(binPath + " describe_repository " + relativeDirectory + " " + commitHash);
    }

    public static Command createDescribeCommitCommand(String binPath, String relativeDirectory, String commitHash) {
        return new LanguageStatsCommand(binPath + " describe_commit " + relativeDirectory + " " + commitHash);
    }

    public LanguageStatsCommand(String command) {
        super(command);
    }

}
