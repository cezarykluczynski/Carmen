CREATE TABLE IF NOT EXISTS carmen.followers_and_followees (
    id uuid,
    user_id int,
    followers_count int,
    followees_count int,
    shared_count int,
    PRIMARY key(id)
);
