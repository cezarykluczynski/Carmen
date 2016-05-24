package com.cezarykluczynski.carmen.rest.dto;

public class Analysis {

    public Analysis(String username, String status) {
        this.username = username;
        this.status = status;
    }

    public String username;
    public String status;

    public String getUsername() {
        return username;
    }

    public String getStatus() {
        return status;
    }

}
