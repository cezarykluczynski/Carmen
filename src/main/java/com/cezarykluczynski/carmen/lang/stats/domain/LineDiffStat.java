package com.cezarykluczynski.carmen.lang.stats.domain;

public class LineDiffStat {

    private Integer addedLines;

    private Integer removedLines;

    public LineDiffStat(Integer addedLines, Integer removedLines) {
        this.addedLines = addedLines;
        this.removedLines = removedLines;
    }

    public Integer getAddedLines() {
        return addedLines;
    }

    public Integer getRemovedLines() {
        return removedLines;
    }
}
