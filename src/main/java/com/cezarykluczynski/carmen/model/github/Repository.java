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

    private Long githubId;

    private String name;

    private String fullName;

    private String description;

    private String homepage;

    private boolean fork;

    private String defaultBranch;

    private String cloneUrl;

    private Date created;

    private Date updated;

    private Date pushed;

    public Long getGitHubResourceId() {
        return getId();
    }

}
