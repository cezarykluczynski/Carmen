package com.cezarykluczynski.carmen.pojo.rest.api.v1.github.user;

import javax.persistence.Entity;

@Entity
public class BasicProfilePOJO {

    public BasicProfilePOJO(
        String login,
        String name,
        String avatarUrl,
        String company,
        String blog,
        String location,
        String email,
        String bio,
        boolean hireable
    ) {
        this.login = login;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.company = company;
        this.blog = blog;
        this.location = location;
        this.email = email;
        this.bio = bio;
        this.hireable = hireable;
    }

    private String login;
    private String name;
    private String avatarUrl;
    private String company;
    private String  blog;
    private String  location;
    private String  email;
    private String bio;
    private boolean hireable;

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
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

    public String getBio() {
        return bio;
    }

    public boolean getHireable() {
        return hireable;
    }

}
