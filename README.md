Work in progress.

Install using:
```sh
psql -c 'create database carmen;' -U postgres
psql -c "update pg_database set encoding = pg_char_to_encoding('UTF8') where datname = 'carmen';" -U postgres
mvn compile
mvn flyway:migrate
```

Start using:

```sh
mvn tomcat7:run
```