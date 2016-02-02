CREATE TABLE IF NOT EXISTS github_social_stats.followers_being_followees (
    id uuid,
    user_id int,
    follower_being_followee int,
    PRIMARY key(id)
);

CREATE INDEX IF NOT EXISTS ON github_social_stats.followers_being_followees (user_id);
