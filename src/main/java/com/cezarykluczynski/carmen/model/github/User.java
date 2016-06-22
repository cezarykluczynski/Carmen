package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.model.propagations.UserFollowers;
import com.cezarykluczynski.carmen.model.propagations.UserFollowing;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.joda.time.MutableDateTime;

import javax.persistence.*;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity(name = "github.User")
@Table(schema = "github", name = "users")
@ToString(exclude = {"userFollowers", "userFollowing"})
public class User extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private com.cezarykluczynski.carmen.model.users.User user;

    @Column(name = "github_id")
    private Long githubId;

    @Column
    private String name;

    @Column
    private String login;

    @Column
    private Date updated;

    @Column
    private boolean found;

    @Column
    private boolean requested;

    @Column(name = "opt_out")
    private boolean optOut;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "github_type")
    private String type;

    @Column(name = "site_admin")
    private boolean siteAdmin;

    @Column
    private String company;

    @Column
    private String  blog;

    @Column(name = "github_location")
    private String  location;

    @Column
    private String  email;

    @Column
    private boolean hireable;

    @Column
    private String bio;

    @OneToOne(mappedBy="user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserFollowers userFollowers;

    @OneToOne(mappedBy="user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserFollowing userFollowing;

    public boolean canBeUpdated() {
        MutableDateTime yesterday = new MutableDateTime();
        yesterday.addDays(-1);
        Date date = yesterday.toDate();
        return date.after(updated);
    }

}
