CREATE TABLE IF NOT EXISTS carmen.followers_being_followees (
    id uuid,
    user_id int,
    follower_being_followee int,
    PRIMARY key(id)
);

CREATE INDEX IF NOT EXISTS ON carmen.followers_being_followees (user_id);
