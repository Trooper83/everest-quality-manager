# Everest Quality Manager
Open source quality management tool that allows teams to track Bugs, Tests and Releases.

## Setup
1. Install Java
2. Install Postgres
3. Execute `<version>_setup.sql` script for the version found in: `/install/db/sql/`
4. Execute `<version>_insert_person_roles.sql` script found in: `/install/db/sql/`
5. Set Environment Variables
   - EVEREST_DB_USER (database admin user)
   - EVEREST_DB_PASSWORD (database admin user's password)
   - EVEREST_DB_URL (url connection string to database)
9. Start App `java -jar <path_to_jar>`
   - Run the command replacing the path
