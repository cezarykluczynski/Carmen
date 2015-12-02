package com.cezarykluczynski.carmen.git.utils.command;

public class Executor {

    public static Result execute(Command command) {
        return new Result(command);
    }

}
