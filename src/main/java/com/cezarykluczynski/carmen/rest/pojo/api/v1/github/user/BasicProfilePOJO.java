package com.cezarykluczynski.carmen.pojo.rest.api.v1.github.user;

import javax.persistence.Entity;

@Entity
public class BasicProfilePOJO {

    public BasicProfilePOJO() {
    }

    public BasicProfilePOJO(
        String login,
        String name,
        String avatarUrl,
        String type,
        boolean siteAdmin,
        String company,
        String blog,
        String location,
        String email,
        boolean hireable
    ) {
        this.login = login;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.type = type;
        this.siteAdmin = siteAdmin;
        this.company = company;
        this.blog = blog;
        this.location = location;
        this.email = email;
        this.hireable = hireable;
    }

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

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public boolean getRequested() {
        return requested;
    }

    public boolean getOptOut() {
        return optOut;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getType() {
        return type;
    }

    public boolean getSiteAdmin() {
        return siteAdmin;
    }

    public String getCompany() {
        return company;
    }

    public String getBlog() {
        return blog;
    }

    public String getLocation() {
        return location;
    }

    public String getEmail() {
        return email;
    }

    public boolean getHireable() {
        return hireable;
    }

    public String getBio() {
        return bio;
    }

}
