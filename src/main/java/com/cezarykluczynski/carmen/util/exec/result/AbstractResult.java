package com.cezarykluczynski.carmen.util.exec.result;

import com.cezarykluczynski.carmen.util.exec.ErrorReason;

abstract public class AbstractResult {

    protected boolean successful;

    protected String output;

    protected String errorMessage;

    protected ErrorReason errorReason;

    public boolean isSuccessFul() {
        return successful;
    }

    public ErrorReason getErrorReason() {
        return errorReason;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getOutput() {
        return output;
    }

    protected void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    protected void setErrorReason(ErrorReason errorReason) {
        this.errorReason = errorReason;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected void setOutput(String output) {
        this.output = output;
    }
}
