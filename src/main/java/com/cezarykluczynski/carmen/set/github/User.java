package com.cezarykluczynski.carmen.set.github;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {

    private Long id;

    private String login;

    private String name;

    private boolean requested;

    private boolean optOut;

    private String avatarUrl;

    private String type;

    private boolean siteAdmin;

    private String company;

    private String  blog;

    private String  location;

    private String  email;

    private boolean hireable;

    private String bio;

    public boolean exists() {
        return id != null;
    }
}
