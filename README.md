# Everest Test Case & Bug Tracking Tool

## Database 

### Setup
For new databases pgcrypto module must be added through the create extension workflow in pgadmin
1. Right-click `Extensions`
2. Select `Create`
3. Select `Extension`
4. Search for `pgcrypto`
5. Save

### Migrations

```bash
# create a diff between db and domain classes
grails -Dgrails.env={env} dbm-gorm-diff {file.groovy} --add

# execute the changelog / update the db
grails -Dgrails.env={env} dbm-update

# sync db / record changes have been executed
grails -Dgrails.env={env} dbm-changelog-sync
```

## Local Development
1. Install jdk
2. Install grails
3. Download code from repo

### Upgrading Gradle
To upgrade the gradle version locally modify the version number in the url of `./gradle/wrapper/gradle-wrapper.properties`