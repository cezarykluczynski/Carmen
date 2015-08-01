package carmen.model.github;

import javax.persistence.*;

import java.util.Date;

import org.joda.time.MutableDateTime;

@Entity(name = "github.User")
@Table(schema = "github", name = "users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private carmen.model.users.User user;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public carmen.model.users.User getUser() {
        return user;
    }

    public void setUser(carmen.model.users.User user) {
        user = this.user;
    }

    public Long getGithubId() {
        return githubId;
    }

    public void setGithubId(Long githubId) {
        this.githubId = githubId;
    }

    public void setGithubId() {
        this.githubId = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setUpdated() {
        updated = new Date();
    }

    public boolean getFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public boolean canBeUpdated() {
        MutableDateTime yesterday = new MutableDateTime();
        yesterday.addDays(-1);
        Date date = yesterday.toDate();
        return date.after(updated);
    }
}