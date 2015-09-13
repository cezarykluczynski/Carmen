package com.cezarykluczynski.carmen.model.propagations;

import javax.persistence.*;

import java.util.Date;

import com.cezarykluczynski.carmen.model.github.User;

@Entity(name = "propagations.UserFollowing")
@Table(schema = "propagations", name = "user_following")
public class UserFollowing implements Propagation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    @Column
    private String phase;

    @Column
    private Integer page;

    @Column
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setUpdated() {
        setUpdated(new Date());
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPage() {
        return page;
    }

}
