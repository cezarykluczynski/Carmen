package com.cezarykluczynski.carmen.util.exec.executor;

import com.cezarykluczynski.carmen.util.exec.command.ApacheCommonsCommand;
import com.cezarykluczynski.carmen.util.exec.command.Command;
import com.cezarykluczynski.carmen.util.exec.result.ApacheCommonsResult;
import com.cezarykluczynski.carmen.util.exec.result.ProcessBuilderResult;
import com.cezarykluczynski.carmen.util.exec.result.Result;

public class Executor {

    public static Result execute(Command command) {
        if (command instanceof ApacheCommonsCommand) {
            return new ApacheCommonsResult(command);
        } else {
            return new ProcessBuilderResult(command);
        }
    }

}
