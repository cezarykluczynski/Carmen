package carmen.dao.github;

import carmen.model.github.RateLimit;

import java.util.Map;

public interface RateLimitDAO {

    public RateLimit create(carmen.set.github.RateLimit rateLimit);

    public RateLimit getCoreLimit();

    public void decrementRateLimitRemainingCounter();

    public RateLimit getSearchLimit();

    public void deleteOldLimits(String resource);
}
