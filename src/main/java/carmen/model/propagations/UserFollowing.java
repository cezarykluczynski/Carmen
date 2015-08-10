package carmen.model.propagations;

import javax.persistence.*;

import java.util.Date;

import carmen.model.github.User;

@Entity(name = "propagations.UserFollowing")
@Table(schema = "propagations", name = "user_following")
public class UserFollowing {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id")
    private User user;

    @Column
    private String phase;

    @Column
    private Integer page;

    @Column
    private Date updated;

    public void setUpdated() {
        updated = new Date();
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
