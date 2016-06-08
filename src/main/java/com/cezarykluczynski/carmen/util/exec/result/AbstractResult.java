package com.cezarykluczynski.carmen.util.exec.result;

abstract public class AbstractResult {

    protected boolean successful;

    protected String output;

    protected String errorMessage;

    public boolean isSuccessFul() {
        return successful;
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

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected void setOutput(String output) {
        this.output = output;
    }
}
