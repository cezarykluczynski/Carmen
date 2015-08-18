package com.cezarykluczynski.carmen.set.github;

import javax.persistence.Entity;

@Entity
public class User {

    public User(
        Long id,
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
        this.id = id;
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

    public User(Object id, String login) {
        this.id = null;
        this.login = login;
        this.name = "";
        this.avatarUrl = "";
        this.type = "";
        this.siteAdmin = false;
        this.company = "";
        this.blog = "";
        this.location = "";
        this.email = "";
        this.hireable = false;
    }

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

    public Long getId() {
        return id;
    }

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

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

    public boolean exists() {
        return id != null;
    }
}