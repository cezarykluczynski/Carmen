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

    public boolean getRequested() {
        return requested;
    }

    public void setRequested(boolean requested) {
        this.requested = requested;
    }

    public boolean getOptOut() {
        return optOut;
    }

    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getSiteAdmin() {
        return siteAdmin;
    }

    public void setSiteAdmin(boolean siteAdmin) {
        this.siteAdmin = siteAdmin;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getHireable() {
        return hireable;
    }

    public void setHireable(boolean hireable) {
        this.hireable = hireable;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean canBeUpdated() {
        MutableDateTime yesterday = new MutableDateTime();
        yesterday.addDays(-1);
        Date date = yesterday.toDate();
        return date.after(updated);
    }
}