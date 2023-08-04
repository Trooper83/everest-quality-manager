# Everest Test Case & Bug Tracking Tool

## Versions
| Tool        | Version     |
| ----------- | ----------- |
|Grails     |5.3.3          |
|Postgres   |7.5            |
|JDK        |11             |

## Database 

### User/Password
The Everest database requires the following user with elevate permissions
- user: everest_admin
- pass: 

### Setup
For new databases pgcrypto module must be added through the create extension workflow in pgadmin
1. Right-click `Extensions` from the database you wish to add the extension to
2. Select `Create`
3. Select `Extension`
4. Search for `pgcrypto`
5. Save

### Creating Model DB
For new installs of Everest a model database with seeded users and roles is required. Each time the database schema is
modified a new model should be created. To create the model run app in model_db environment:
```bash
gradlew bootRun -Dgrails.env=model_db
```

### Creating Restore File
1. In pgAdmin right-click the database
2. Select Restore
3. Modify options and submit

### Migrations

```bash
# create a diff between db and domain classes
grails -Dgrails.env={env} dbm-gorm-diff {file.groovy} --add

# execute the changelog / update the db
grails -Dgrails.env={env} dbm-update

# sync db / record changes have been executed
grails -Dgrails.env={env} dbm-changelog-sync
```
-----
## Local Development
1. Install jdk
2. Install grails
3. Download code from repo

### Upgrading Gradle
To upgrade the gradle version locally modify the version number in the url of `./gradle/wrapper/gradle-wrapper.properties`

--------
## Customer Installs

### Initial Database Setup
1. Install postgres
2. Postgres should be running at `localhost:5432`
3. Change `root` password
4. Create user `everest_admin` with all permissions
5. Create database named `everest`
6. Restore the database using the model_db file

### Updating Database
1. Use PG Schema Diff tool to compare source db (model_db) -> target (db to update)
2. Generate script
3. Modify script if needed
4. Execute script against target db