package com.cezarykluczynski.carmen.set.github;

import com.cezarykluczynski.carmen.util.github.GitHubResource;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class RepositoryDTO implements GitHubResource {

    private Long id;

    private Long parentId;

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
