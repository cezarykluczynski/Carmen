package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity(name = "github.RepositoryClone")
@Table(schema = "github", name = "repositories_clones")
public class RepositoryClone extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="github_repository_id", referencedColumnName = "id", unique = true, nullable = false)
    private Repository repository;

    @OneToOne
    @JoinColumn(name="parent_id")
    private RepositoryClone parent;

    @Column(name="location_directory")
    private String locationDirectory;

    @Column(name="location_subdirectory")
    private String locationSubdirectory;

    @Column(name="server_id")
    private String serverId;

    @Column
    private Date cloned;

    @Column
    private Date updated;

    @Column(name = "commits_statistics_until")
    private Date commitsStatisticsUntil;

}
