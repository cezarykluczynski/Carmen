package com.cezarykluczynski.carmen.util.exec.result;

import com.cezarykluczynski.carmen.util.exec.ErrorReason;

public interface Result {

    boolean isSuccessFul();

    ErrorReason getErrorReason();

    String getErrorMessage();

    String getOutput();

}
