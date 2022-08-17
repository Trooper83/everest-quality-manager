databaseChangeLog = {

    changeSet(author: "UNoft (generated)", id: "1660712053558-1") {
        createSequence(incrementBy: "1", sequenceName: "hibernate_sequence", startValue: "1")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-2") {
        createTable(tableName: "area") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "areaPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-3") {
        createTable(tableName: "bug") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "bugPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "platform", type: "VARCHAR(7)")

            column(name: "person_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "status", type: "VARCHAR(6)") {
                constraints(nullable: "false")
            }

            column(name: "description", type: "VARCHAR(1000)")

            column(name: "project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "area_id", type: "BIGINT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-4") {
        createTable(tableName: "bug_environment") {
            column(name: "bug_environments_id", type: "BIGINT")

            column(name: "environment_id", type: "BIGINT")

            column(name: "environments_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-5") {
        createTable(tableName: "bug_step") {
            column(name: "bug_steps_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "step_id", type: "BIGINT")

            column(name: "steps_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-6") {
        createTable(tableName: "environment") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "environmentPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-7") {
        createTable(tableName: "iteration_step") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "iteration_stepPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "action", type: "VARCHAR(500)")

            column(name: "result", type: "VARCHAR(500)")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-8") {
        createTable(tableName: "person") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "personPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "password_expired", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "account_locked", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "password", type: "VARCHAR(256)") {
                constraints(nullable: "false")
            }

            column(name: "account_expired", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "enabled", type: "BOOLEAN") {
                constraints(nullable: "false")
            }

            column(name: "email", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-9") {
        createTable(tableName: "person_role") {
            column(name: "person_id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "person_rolePK")
            }

            column(name: "role_id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "person_rolePK")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-10") {
        createTable(tableName: "project") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "projectPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "code", type: "VARCHAR(3)") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(100)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-11") {
        createTable(tableName: "project_area") {
            column(name: "project_areas_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "area_id", type: "BIGINT")

            column(name: "areas_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-12") {
        createTable(tableName: "project_environment") {
            column(name: "project_environments_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "environment_id", type: "BIGINT")

            column(name: "environments_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-13") {
        createTable(tableName: "registration_code") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "registration_codePK")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "username", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "token", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-14") {
        createTable(tableName: "release_plan") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "release_planPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(500)") {
                constraints(nullable: "false")
            }

            column(name: "release_date", type: "timestamp")

            column(name: "status", type: "VARCHAR(11)") {
                constraints(nullable: "false")
            }

            column(name: "planned_date", type: "timestamp")

            column(name: "project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-15") {
        createTable(tableName: "role") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "rolePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "authority", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-16") {
        createTable(tableName: "scenario") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "scenarioPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "platform", type: "VARCHAR(7)")

            column(name: "person_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "execution_method", type: "VARCHAR(9)")

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(3)")

            column(name: "gherkin", type: "VARCHAR(2500)")

            column(name: "description", type: "VARCHAR(1000)")

            column(name: "project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "area_id", type: "BIGINT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-17") {
        createTable(tableName: "scenario_environment") {
            column(name: "scenario_environments_id", type: "BIGINT")

            column(name: "environment_id", type: "BIGINT")

            column(name: "environments_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-18") {
        createTable(tableName: "step") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "stepPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "action", type: "VARCHAR(500)")

            column(name: "result", type: "VARCHAR(500)")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-19") {
        createTable(tableName: "test_case") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "test_casePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "platform", type: "VARCHAR(7)")

            column(name: "person_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "execution_method", type: "VARCHAR(9)")

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "type", type: "VARCHAR(3)")

            column(name: "description", type: "VARCHAR(1000)")

            column(name: "project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "area_id", type: "BIGINT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-20") {
        createTable(tableName: "test_case_environment") {
            column(name: "test_case_environments_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "environment_id", type: "BIGINT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-21") {
        createTable(tableName: "test_case_step") {
            column(name: "test_case_steps_id", type: "BIGINT")

            column(name: "step_id", type: "BIGINT")

            column(name: "steps_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-22") {
        createTable(tableName: "test_case_test_groups") {
            column(name: "test_group_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "test_case_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "test_groups_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-23") {
        createTable(tableName: "test_cycle") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "test_cyclePK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "environ_id", type: "BIGINT")

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(500)") {
                constraints(nullable: "false")
            }

            column(name: "platform", type: "VARCHAR(7)")

            column(name: "release_plan_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-24") {
        createTable(tableName: "test_cycle_test_case_ids") {
            column(name: "test_cycle_id", type: "BIGINT")

            column(name: "test_case_ids_long", type: "BIGINT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-25") {
        createTable(tableName: "test_group") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "test_groupPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "date_created", type: "timestamp") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "project_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-26") {
        createTable(tableName: "test_iteration") {
            column(autoIncrement: "true", name: "id", type: "BIGINT") {
                constraints(nullable: "false", primaryKey: "true", primaryKeyName: "test_iterationPK")
            }

            column(name: "version", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "name", type: "VARCHAR(255)") {
                constraints(nullable: "false")
            }

            column(name: "notes", type: "VARCHAR(1000)")

            column(name: "test_case_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "result", type: "VARCHAR(4)") {
                constraints(nullable: "false")
            }

            column(name: "test_cycle_id", type: "BIGINT") {
                constraints(nullable: "false")
            }
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-27") {
        createTable(tableName: "test_iteration_iteration_step") {
            column(name: "test_iteration_steps_id", type: "BIGINT") {
                constraints(nullable: "false")
            }

            column(name: "iteration_step_id", type: "BIGINT")

            column(name: "steps_idx", type: "INT")
        }
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-28") {
        addUniqueConstraint(columnNames: "email", constraintName: "UC_PERSONEMAIL_COL", tableName: "person")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-29") {
        addUniqueConstraint(columnNames: "code", constraintName: "UC_PROJECTCODE_COL", tableName: "project")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-30") {
        addUniqueConstraint(columnNames: "name", constraintName: "UC_PROJECTNAME_COL", tableName: "project")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-31") {
        addUniqueConstraint(columnNames: "authority", constraintName: "UC_ROLEAUTHORITY_COL", tableName: "role")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-32") {
        addForeignKeyConstraint(baseColumnNames: "test_case_environments_id", baseTableName: "test_case_environment", constraintName: "FK4ajnc7fbdc2iyj2k8ytmga4h0", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "test_case", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-33") {
        addForeignKeyConstraint(baseColumnNames: "area_id", baseTableName: "test_case", constraintName: "FK5a1omo3b7vn82yqckgn76cf5", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "area", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-34") {
        addForeignKeyConstraint(baseColumnNames: "area_id", baseTableName: "scenario", constraintName: "FK6hp1q8kdvfd0bs6vtr5k5ddui", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "area", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-35") {
        addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "release_plan", constraintName: "FK6y0f94nne0acxxhkpy2xy30uv", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "project", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-36") {
        addForeignKeyConstraint(baseColumnNames: "area_id", baseTableName: "bug", constraintName: "FK8hnx6x9kyglxi85uqadpw4cgd", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "area", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-37") {
        addForeignKeyConstraint(baseColumnNames: "step_id", baseTableName: "test_case_step", constraintName: "FK9lnmhnxmhiopdnc8vdfg7sh1x", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "step", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-38") {
        addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "bug", constraintName: "FKacv21dyrr01us0cwcf4erfil3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-39") {
        addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "scenario", constraintName: "FKadgolvhsnmgr7cr2wv6x415pt", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-40") {
        addForeignKeyConstraint(baseColumnNames: "area_id", baseTableName: "project_area", constraintName: "FKbf6m2u4tapd9cenngqjyqc774", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "area", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-41") {
        addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "test_case", constraintName: "FKbxo28q05096mw3d323ty0bemf", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-42") {
        addForeignKeyConstraint(baseColumnNames: "test_case_id", baseTableName: "test_iteration", constraintName: "FKcit7tt10rkk7ogyco9b3mpueb", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "test_case", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-43") {
        addForeignKeyConstraint(baseColumnNames: "iteration_step_id", baseTableName: "test_iteration_iteration_step", constraintName: "FKd2v1cpt7rug1iuhwcfhnf38s2", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "iteration_step", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-44") {
        addForeignKeyConstraint(baseColumnNames: "test_cycle_id", baseTableName: "test_cycle_test_case_ids", constraintName: "FKdbiqaqks2t9i0ftwighc4puqk", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "test_cycle", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-45") {
        addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "test_group", constraintName: "FKexhynrfen81eaqi6a0s8ssch3", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "project", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-46") {
        addForeignKeyConstraint(baseColumnNames: "test_case_id", baseTableName: "test_case_test_groups", constraintName: "FKgh822uqx2oba5m9j6swv3fxvm", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "test_case", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-47") {
        addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "scenario", constraintName: "FKgk8xds3ylwmbls60s1chl8ssc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "project", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-48") {
        addForeignKeyConstraint(baseColumnNames: "step_id", baseTableName: "bug_step", constraintName: "FKh36t02m1dag5n7q0ons4jqck", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "step", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-49") {
        addForeignKeyConstraint(baseColumnNames: "person_id", baseTableName: "person_role", constraintName: "FKhyx1efsls0f00lxs6xd4w2b3j", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "person", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-50") {
        addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "bug", constraintName: "FKi91cmp5c6v9yv26iow1g0emxc", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "project", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-51") {
        addForeignKeyConstraint(baseColumnNames: "project_id", baseTableName: "test_case", constraintName: "FKit9gxtn7qhwml7ni05l11syb8", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "project", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-52") {
        addForeignKeyConstraint(baseColumnNames: "environment_id", baseTableName: "bug_environment", constraintName: "FKjvuapln2f2nlgf8mnuognc310", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "environment", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-53") {
        addForeignKeyConstraint(baseColumnNames: "environment_id", baseTableName: "project_environment", constraintName: "FKl74d0tcnx5nulhfueh8xysjic", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "environment", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-54") {
        addForeignKeyConstraint(baseColumnNames: "environment_id", baseTableName: "scenario_environment", constraintName: "FKl8u6rrw8uirq8jxsa9l0uxerg", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "environment", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-55") {
        addForeignKeyConstraint(baseColumnNames: "test_cycle_id", baseTableName: "test_iteration", constraintName: "FKm4mccqoa1hnrjktjkrk4dii21", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "test_cycle", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-56") {
        addForeignKeyConstraint(baseColumnNames: "environment_id", baseTableName: "test_case_environment", constraintName: "FKnicc7xvcirwj0kqmhqxsgj3mp", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "environment", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-57") {
        addForeignKeyConstraint(baseColumnNames: "test_group_id", baseTableName: "test_case_test_groups", constraintName: "FKohp0n1i8nxh8226dfiu9tte7", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "test_group", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-58") {
        addForeignKeyConstraint(baseColumnNames: "release_plan_id", baseTableName: "test_cycle", constraintName: "FKpx73o7axguyhq6debxatfvxmf", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "release_plan", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-59") {
        addForeignKeyConstraint(baseColumnNames: "role_id", baseTableName: "person_role", constraintName: "FKs7asxi8amiwjjq1sonlc4rihn", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "role", validate: "true")
    }

    changeSet(author: "UNoft (generated)", id: "1660712053558-60") {
        addForeignKeyConstraint(baseColumnNames: "environ_id", baseTableName: "test_cycle", constraintName: "FKtch3psrkx6lpc3byyppdhlnoi", deferrable: "false", initiallyDeferred: "false", referencedColumnNames: "id", referencedTableName: "environment", validate: "true")
    }
}
