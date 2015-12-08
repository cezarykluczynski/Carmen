package com.cezarykluczynski.carmen.model.github;

import com.cezarykluczynski.carmen.model.CarmenRelationalEntity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "github.RepositoryClone")
@Table(schema = "github", name = "repositories_clones")
public class RepositoryClone extends CarmenRelationalEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name="github_repository_id", referencedColumnName = "id", unique = true, nullable = false)
    private Repository repository;

    @OneToOne(optional=true)
    @JoinColumn(name="parent_id", nullable=true)
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


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Repository getRepository() {
        return repository;
    }

    public void setRepository(Repository repository) {
        this.repository = repository;
    }

    public RepositoryClone getParent() {
        return parent;
    }

    public void setParent(RepositoryClone parent) {
        this.parent = parent;
    }

    public String getLocationDirectory() {
        return locationDirectory;
    }

    public void setLocationDirectory(String locationDirectory) {
        this.locationDirectory = locationDirectory;
    }

    public String getLocationSubdirectory() {
        return locationSubdirectory;
    }

    public void setLocationSubdirectory(String locationSubdirectory) {
        this.locationSubdirectory = locationSubdirectory;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Date getCloned() {
        return cloned;
    }

    public void setCloned(Date cloned) {
        this.cloned = cloned;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

}
