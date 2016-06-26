package com.cezarykluczynski.carmen.vcs.git.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class CommitHash {

    private String hash;

    private Date date;

    private String committerEmail;

}
