Work in progress.

## Status

[![CircleCI](https://circleci.com/gh/cezarykluczynski/Carmen.svg?style=svg)](https://circleci.com/gh/cezarykluczynski/Carmen)
[![codecov](https://codecov.io/gh/cezarykluczynski/Carmen/branch/master/graph/badge.svg)](https://codecov.io/gh/cezarykluczynski/Carmen)

## Development

For higher GitHub API limits, set CARMEN_GITHUB_ACCESS_TOKEN environment variable
to token obtained from https://github.com/settings/tokens,
otherwise API limits could easily be exceeded.
This is your personal token. Do not commit it.

## Installation
Install using:
```sh
cp src/main/resources/example/application.properties src/main/resources/application.properties
gradle cassandraMigrate
psql -c 'create database carmen;' -U postgres
gradle flywayMigrate -i
npm install --global gulp
cd src/main/resources/static && npm install && gulp
```

You may need to customize PostgreSQL and Cassandra credendials.

## Startup
Start using:

```sh
gradle bootRun
```

## Tests

### Java tests

```sh
gradle test
```

### TypeScript tests

```sh
cd src/main/resources/static && npm install && grunt to
```

### Ruby

```sh
rspec
```
