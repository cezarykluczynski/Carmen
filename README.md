Work in progress.

## Status

[![Build Status](https://travis-ci.org/cezarykluczynski/Carmen.svg?branch=master)](https://travis-ci.org/cezarykluczynski/Carmen)

## Development

For higher GitHub API limits, set CARMEN_GITHUB_ACCESS_TOKEN environment variable
to token obtained from https://github.com/settings/tokens,
otherwise API limits could easily be exceeded.
This is your personal token. Do not commit it.

## Installation
Install using:
```sh
cp src/main/resources/example/cassandra.properties src/main/resources/cassandra.properties
mvn exec:java -Dexec.mainClass="com.cezarykluczynski.carmen.db.CassandraMigrations"
psql -c 'create database carmen;' -U postgres
mvn compile
mvn flyway:migrate
npm install --global gulp
npm install
gulp
```

You may need to customize PostgreSQL and Cassandra credendials.

## Startup
Start using:

```sh
mvn tomcat7:run
```

## Tests

```sh
mvn test
```