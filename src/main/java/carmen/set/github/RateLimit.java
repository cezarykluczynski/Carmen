package carmen.set.github;

import javax.persistence.Entity;

import java.util.Date;

@Entity
public class RateLimit {

    public RateLimit(Integer limit, Integer remaining, Date reset) {
        this.limit = limit;
        this.remaining = remaining;
        this.reset = reset;
    }

    public Integer limit;
    public Integer remaining;
    public Date reset;

    public Integer getLimit() {
        return limit;
    }

    public Integer getRemaining() {
        return remaining;
    }

    public Date getReset() {
        return reset;
    }
}