package com.everlution

import grails.gorm.transactions.Transactional

class BootStrap {

    def init = { servletContext ->
        environments {
            test {
                seedTestUsers()
                seedTestData()
            }
            development {
                seedTestUsers()
                seedTestData()
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
    void seedTestData() {
        def area = new Area(name: "bootstrap area")
        def area1 = new Area(name: "bootstrap area 12")
        def project = new Project(name: "bootstrap project", code: "bsp", areas: [area]).save()
        def project1 = new Project(name: "bootstrap project12", code: "BPP", areas: [area1]).save()
        Step testStep = new Step(action: "do something", result: "something happened").save(failOnError: true)
        Step testStep1 = new Step(action: "do something12", result: "something happened12").save(failOnError: true)
        Step testStep2 = new Step(action: "do something123", result: "something happened123").save(failOnError: true)
        new TestCase(creator: "test", name: "everest", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep, testStep1],
                project: project, area: area).save(failOnError: true)
        new TestCase(creator: "test123", name: "everest123", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep2],
                project: project1, area: area1).save(failOnError: true)
        def bugStep = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep1 = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep2 = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep3 = new Step(action: "do something", result: "something happened").save(failOnError: true)
        new Bug(creator: "bug creator", name: "seeded bug 1", description: "description of the bug",
                project: project1, steps: [bugStep], area: area1).save(failOnError: true)
        new Bug(creator: "bug creator1", name: "seeded bug 2", description: "description of the bug 1",
                project: project, steps: [bugStep1], area: area).save(failOnError: true)
        new Bug(creator: "bug creator2", name: "seeded bug 3", description: "description of the bug 2",
                project: project1, steps: [bugStep2, bugStep3], area: area1).save(failOnError: true)
    }
}
