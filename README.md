# crossward
Backend crossword REST API to Store and maintain crossword data. See the related telegram [leaderboard bot](https://github.com/RobertWetzler/CrosswordLeaderboardBot).

## Tech Stack
| Dependency | Version |
| ---------- | ------- |
| Kotlin     | 1.5.10  |
| Java       | 16      |
| Springboot | 2.5.1   |

## Local Development

### Creating the Sqlite database
Create the database at the root of the project:
```shell
> sqlite3 Crossward.db
```
Run the ddl script to create the tables
```shell
> cat src/main/resources/ddl.sql | sqlite3 Crossward.db
```

Create an `application-local.properties` file in the resources folder:
```properties
# Database
driverClassName=org.sqlite.JDBC
url=jdbc:sqlite:Crossward.db
username=sa
password=sa
```

### Build & Run
Build using gradle
```shell
> ./gradlew build
```

Run using the bootRun task
```shell
> ./gradlew bootRun --args='--spring.profiles.active=local'
```

## Example requests
```shell
curl --request GET \
  --url http://localhost:8080/crossward/api/users/ids/1
  
curl --request GET \
  --url http://localhost:8080/crossward/api/users/names/Billy
```