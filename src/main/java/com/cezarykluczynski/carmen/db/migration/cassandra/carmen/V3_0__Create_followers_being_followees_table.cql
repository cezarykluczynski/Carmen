CREATE TABLE IF NOT EXISTS carmen.followers_being_followees (
    id uuid,
    user_id bigint,
    follower_being_followee int,
    PRIMARY key(id)
);
