package com.cezarykluczynski.carmen.util.exec;

public class LanguageStatsCommand extends Command {

    public static Command createSupportedLanguagesCommand(String binPath) {
        return new LanguageStatsCommand(binPath + " supported_languages");
    }

    public LanguageStatsCommand(String command) {
        super(command);
    }

}
