package com.everlution

import grails.gorm.transactions.Transactional

class BootStrap {

    def init = { servletContext ->
        environments {
            test {
                seedTestUsers()
                seedTestCases()
            }
            development {
                seedTestUsers()
                seedTestCases()
            }
        }
    }

    def destroy = {
    }

    @Transactional
    void seedTestUsers() {
        def basicRole = new Role(authority: "ROLE_BASIC").save(failOnError: true)
        def readOnlyRole = new Role(authority: "ROLE_READ_ONLY").save(failOnError: true)
        def projectAdminRole = new Role(authority: "ROLE_PROJECT_ADMIN").save(failOnError: true)
        def orgAdminRole = new Role(authority: "ROLE_ORG_ADMIN").save(failOnError: true)
        def appAdminRole = new Role(authority: "ROLE_APP_ADMIN").save(failOnError: true)

        def readOnlyUser = new Person(username: "read_only", password: "password").save(failOnError: true)
        def basicUser = new Person(username: "basic", password: "password").save(failOnError: true)
        def projectAdminUser = new Person(username: "project_admin", password: "password").save(failOnError: true)
        def orgAdminUser = new Person(username: "org_admin", password: "password").save(failOnError: true)
        def appAdminUser = new Person(username: "app_admin", password: "password").save(failOnError: true)

        PersonRole.create(readOnlyUser, readOnlyRole)
        PersonRole.create(basicUser, basicRole)
        PersonRole.create(projectAdminUser, projectAdminRole)
        PersonRole.create(orgAdminUser, orgAdminRole)
        PersonRole.create(appAdminUser, appAdminRole)

        PersonRole.withSession {
            it.flush()
            it.clear()
        }

    }

    void seedTestCases() {
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save(failOnError: true)
        TestStep testStep1 = new TestStep(action: "do something1", result: "something happened1").save(failOnError: true)
        TestCase testCase = new TestCase(creator: "test", name: "everest", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep, testStep1])
        testCase.save(failOnError: true)
    }
}
