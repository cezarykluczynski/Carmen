Work in progress.

## Development

For higher GitHub API limits, set CARMEN_GITHUB_ACCESS_TOKEN environment variable
to token obtained from https://github.com/settings/tokens,
otherwise API limits could easily be exceeded.
This is your personal token. Do not commit it.

## Installation
Install using:
```sh
psql -c 'create database carmen;' -U postgres
psql -c "update pg_database set encoding = pg_char_to_encoding('UTF8') where datname = 'carmen';" -U postgres
mvn compile
mvn flyway:migrate
npm install --global gulp
gulp
```

Start using:

```sh
mvn tomcat7:run
```