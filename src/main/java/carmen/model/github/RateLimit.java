package carmen.model.github;

import javax.persistence.*;

import java.util.Date;

@Entity(name = "github.RateLimit")
@Table(schema = "github", name = "rate_limits")
public class RateLimit {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String resource;

    @Column(name = "resource_limit")
    private Integer limit;

    @Column
    private Integer remaining;

    @Column(name = "resource_reset")
    private Date reset;

    @Column
    private Date updated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getResource()
    {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public void setRemaining(Integer remaining) {
        this.remaining = remaining;
    }

    public Date getReset() {
        return reset;
    }

    public void setReset(Date reset) {
        this.reset = reset;
    }

    public void setUpdated() {
        updated = new Date();
    }
}
