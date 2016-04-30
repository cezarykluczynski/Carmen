CREATE TABLE IF NOT EXISTS repositories.commits (
    id uuid,
    user_id int,
    repository_id int,
    hash varchar,
    PRIMARY key(id)
);
