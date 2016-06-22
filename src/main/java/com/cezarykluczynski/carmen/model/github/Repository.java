package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import com.cezarykluczynski.carmen.util.github.GitHubResource;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;

import java.util.Date;

@Data
@Entity(name = "github.Repository")
@Table(schema = "github", name = "repositories")
@ToString(exclude = {"user", "repositoryClone"})
public class Repository extends CarmenRelationalEntity implements GitHubResource {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne
    @JoinColumn(name = "github_user_id", referencedColumnName = "id")
    private User user;

    @OneToOne
    @JoinColumn(name="parent_id")
    private Repository parent;

    @OneToOne(mappedBy = "repository")
    @JoinColumn
    private RepositoryClone repositoryClone;

    @Column(name = "github_id")
    private Long githubId;

    @Column
    private String name;

    @Column(name = "full_name")
    private String fullName;

    @Column
    private String description;

    @Column
    private String homepage;

    @Column
    private boolean fork;

    @Column(name = "default_branch")
    private String defaultBranch;

    @Column(name = "clone_url")
    private String cloneUrl;

    @Column
    private Date created;

    @Column
    private Date updated;

    @Column
    private Date pushed;

    public Long getGitHubResourceId() {
        return getId();
    }

}
