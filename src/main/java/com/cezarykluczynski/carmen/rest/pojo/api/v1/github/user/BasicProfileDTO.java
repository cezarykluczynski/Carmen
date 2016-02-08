package com.cezarykluczynski.carmen.rest.pojo.api.v1.github.user;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;

@Entity
@Builder
@Data
public class BasicProfileDTO {

    private String login;

    private String name;

    private String avatarUrl;

    private String company;

    private String  blog;

    private String  location;

    private String  email;

    private String bio;

    private boolean hireable;

}
