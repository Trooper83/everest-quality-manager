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

    @Transactional
    void seedTestCases() {
        def project = new Project(name: "bootstrap project", code: "bsp").save()
        def project1 = new Project(name: "bootstrap project12", code: "BPP").save()
        Step testStep = new Step(action: "do something", result: "something happened").save(failOnError: true)
        Step testStep1 = new Step(action: "do something12", result: "something happened12").save(failOnError: true)
        Step testStep2 = new Step(action: "do something123", result: "something happened123").save(failOnError: true)
        new TestCase(creator: "test", name: "everest", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep, testStep1], project: project).save(failOnError: true)
        new TestCase(creator: "test123", name: "everest123", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep2], project: project1).save(failOnError: true)
    }
}
