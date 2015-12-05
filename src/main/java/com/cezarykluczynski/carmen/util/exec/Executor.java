package com.cezarykluczynski.carmen.util.exec;

public class Executor {

    public static Result execute(Command command) {
        return new Result(command);
    }

}
