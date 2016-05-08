Work in progress.

## Status

[![Build Status](https://travis-ci.org/cezarykluczynski/Carmen.svg?branch=master)](https://travis-ci.org/cezarykluczynski/Carmen)
[![codecov](https://codecov.io/gh/cezarykluczynski/Carmen/branch/master/graph/badge.svg)](https://codecov.io/gh/cezarykluczynski/Carmen)

## Development

For higher GitHub API limits, set CARMEN_GITHUB_ACCESS_TOKEN environment variable
to token obtained from https://github.com/settings/tokens,
otherwise API limits could easily be exceeded.
This is your personal token. Do not commit it.

## Installation
Install using:
```sh
cp src/main/resources/example/config.properties src/main/resources/config.properties
mvn exec:java -Dexec.mainClass="com.cezarykluczynski.carmen.db.CassandraMigrations"
psql -c 'create database carmen;' -U postgres
mvn clean compile
mvn flyway:migrate
npm install --global gulp
npm install
gulp
```

You may need to customize PostgreSQL and Cassandra credendials.

## Startup
Start using:

```sh
mvn clean tomcat7:run
```

## Tests

### Java tests
There are unit tests and integration tests.

```sh
mvn clean test
mvn clean verify
```

### TypeScript tests

```sh
cd src/main/webapp/frontend && npm install && grunt to
```

### Ruby

```sh
rspec
```
