Work in progress.

## Status

[![Build Status](https://travis-ci.org/cezarykluczynski/Carmen.svg?branch=master)](https://travis-ci.org/cezarykluczynski/Carmen)
[![codecov.io](http://codecov.io/github/cezarykluczynski/Carmen/coverage.svg?branch=master)](http://codecov.io/github/cezarykluczynski/Carmen?branch=master)

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

There are unit tests and integration tests. Cobertura is used for code coverage.
Integration tests run unit tests by default.

Following Maven phases are available, with or without code coverage:

```sh
mvn test
mvn integration-test
mvn cobertura:cobertura
mvn cobertura:cobertura-integration-test
```