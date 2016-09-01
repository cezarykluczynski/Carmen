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

    private Long githubId;

    private String name;

    private String login;

    private Date updated;

    private boolean found;

    private boolean requested;

    private boolean optOut;

    private String avatarUrl;

    @Column(name = "github_type")
    private String type;

    private boolean siteAdmin;

    private String company;

    private String  blog;

    @Column(name = "github_location")
    private String  location;

    private String  email;

    private boolean hireable;

    private String bio;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserFollowers userFollowers;

    @OneToOne(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private UserFollowing userFollowing;

    public boolean canBeUpdated() {
        MutableDateTime yesterday = new MutableDateTime();
        yesterday.addDays(-1);
        Date date = yesterday.toDate();
        return date.after(updated);
    }

}
