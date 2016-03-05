package com.cezarykluczynski.carmen.rest.dto.api.v1.github.user;

import lombok.Builder;
import lombok.Getter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@Builder
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
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
