databaseChangeLog = {

    changeSet(author: "UNoft (generated)", id: "1678423168822-3") {
        addColumn(tableName: "bug") {
            column(name: "actual", type: "varchar(500)")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1678423168822-4") {
        addColumn(tableName: "bug") {
            column(name: "expected", type: "varchar(500)")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1678423168822-5") {
        addForeignKeyConstraint(baseColumnNames: "project_environments_id", baseTableName: "project_environment", constraintName: "FKohbou9h8qmqvydblecd85d1y1", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "project", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1678423168822-6") {
        addForeignKeyConstraint(baseColumnNames: "project_areas_id", baseTableName: "project_area", constraintName: "FKs14e0fppv93u8edomxmxy6kkm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "project", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1678423168822-7") {
        dropColumn(columnName: "areas_idx", tableName: "project_area")
    }

    changeSet(author: "UNoft (generated)", id: "1678423168822-8") {
        dropColumn(columnName: "environments_idx", tableName: "project_environment")
    }
}
