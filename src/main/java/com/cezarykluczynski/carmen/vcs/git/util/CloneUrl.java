package com.cezarykluczynski.carmen.vcs.git.util;

public class CloneUrl implements Url {

    String url;

    public CloneUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
