package com.cezarykluczynski.carmen.util.exec.result;

public interface Result {

    boolean isSuccessFul();

    String getErrorMessage();

    String getOutput();

}
