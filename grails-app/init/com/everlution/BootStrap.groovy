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
            local {
                seedTestUsers()
                seedTestData()
            }
            integrated {
                // intentionally left blank
            }
            production {
                // intentionally left blank
            }
            model_db {
                initDB()
            }
        }
    }

    def destroy = {
    }

    @Transactional
    void initDB() {
        new Role(authority: "ROLE_BASIC").save(failOnError: true)
        new Role(authority: "ROLE_READ_ONLY").save(failOnError: true)
        new Role(authority: "ROLE_PROJECT_ADMIN").save(failOnError: true)
        new Role(authority: "ROLE_ORG_ADMIN").save(failOnError: true)
        def appAdminRole = new Role(authority: "ROLE_APP_ADMIN").save(failOnError: true)
        def appAdminUser = new Person(email: "app_admin@appadmin.com", password: "!Password#2022").save(failOnError: true)

        PersonRole.create(appAdminUser, appAdminRole)

        PersonRole.withSession {
            it.flush()
            it.clear()
        }
    }

    @Transactional
    void seedTestUsers() {
        def basicRole = new Role(authority: "ROLE_BASIC").save(failOnError: true)
        def readOnlyRole = new Role(authority: "ROLE_READ_ONLY").save(failOnError: true)
        def projectAdminRole = new Role(authority: "ROLE_PROJECT_ADMIN").save(failOnError: true)
        def appAdminRole = new Role(authority: "ROLE_APP_ADMIN").save(failOnError: true)

        def readOnlyUser = new Person(email: "read_only@readonly.com", password: "!Password#2022").save(failOnError: true)
        def basicUser = new Person(email: "basic@basic.com", password: "!Password#2022").save(failOnError: true)
        def projectAdminUser = new Person(email: "project_admin@projectadmin.com", password: "!Password#2022").save(failOnError: true)
        def appAdminUser = new Person(email: "app_admin@appadmin.com", password: "!Password#2022").save(failOnError: true)
        new Person(email: "expired@accountexpired.com", password: "!Password#2022", accountExpired: true).save(failOnError: true)
        new Person(email: "locked@accountlocked.com", password: "!Password#2022", accountLocked: true).save(failOnError: true)
        new Person(email: "expired@passwordexpired.com", password: "!Password#2022", passwordExpired: true).save(failOnError: true)
        new Person(email: "disabled@disabled.com", password: "!Password#2022", enabled: false).save(failOnError: true)

        PersonRole.create(readOnlyUser, readOnlyRole)
        PersonRole.create(basicUser, basicRole)
        PersonRole.create(projectAdminUser, projectAdminRole)
        PersonRole.create(appAdminUser, appAdminRole)

        PersonRole.withSession {
            it.flush()
            it.clear()
        }

    }

    @Transactional
    void seedTestData() {
        def person = new Person(email: "test@bootstrapped.com", password: "!Password#2022").save(failOnError: true)
        def area = new Area(name: "bootstrap area")
        def area1 = new Area(name: "bootstrap area 12")
        def env = new Environment(name: "bootstrap environment")
        def env1 = new Environment(name: "bootstrap environment12")
        def project = new Project(name: "bootstrap project", code: "bsp", areas: [area], environments: [env]).save(failOnError: true)
        def project1 = new Project(name: "bootstrap project12", code: "BPP", areas: [area1], environments: [env1]).save(failOnError: true)
        Step testStep = new Step(act: "do something", result: "something happened", project: project, person: person).save(failOnError: true)
        Step testStep1 = new Step(act: "do something12", result: "something happened12", project: project, person: person).save(failOnError: true)
        Step testStep2 = new Step(act: "do something123", result: "something happened123", project: project1, person: person).save(failOnError: true)
        Step testStep3 = new Step(act: "do something123", result: "something happened123", project: project, person: person).save(failOnError: true)
        Step testStep4 = new Step(act: "do something123", result: "something happened123", project: project1, person: person).save(failOnError: true)
        def grandParent = new Step(act: "grand parent", result: "grand parent result", project: project1,
                person: person, isBuilderStep: true, name: "this is the grand parent step that has spawned all of them").save(failOnError: true)
        def parent = new Step(act: "parent act", result: "parent res", project: project1,
                person: person, isBuilderStep: true, name: "this is the parent step that is a child to the grand parent").save(failOnError: true)
        def uncle = new Step(act: "uncle act", result: "uncle res", project: project1,
                person: person, isBuilderStep: true, name: "this is the uncle step that is child to the grand parent").save(failOnError: true)
        def child = new Step(act: "child act", result: "child res", project: project1,
                person: person, isBuilderStep: true, name: "this is the ultimate child step").save(failOnError: true)

        new Link(ownerId: parent.id, linkedId: child.id, project: project1, relation: Relationship.IS_PARENT_OF.name).save(failOnError: true)
        new Link(ownerId: child.id, linkedId: parent.id, project: project1, relation: Relationship.IS_CHILD_OF.name).save(failOnError: true)
        new Link(ownerId: uncle.id, linkedId: child.id, project: project1, relation: Relationship.IS_PARENT_OF.name).save(failOnError: true)
        new Link(ownerId: child.id, linkedId: uncle.id, project: project1, relation: Relationship.IS_CHILD_OF.name).save(failOnError: true)
        new Link(ownerId: grandParent.id, linkedId: parent.id, project: project1, relation: Relationship.IS_PARENT_OF.name).save(failOnError: true)
        new Link(ownerId: parent.id, linkedId: grandParent.id, project: project1, relation: Relationship.IS_CHILD_OF.name).save(failOnError: true)
        new Link(ownerId: grandParent.id, linkedId: uncle.id, project: project1, relation: Relationship.IS_PARENT_OF.name).save(failOnError: true)
        new Link(ownerId: uncle.id, linkedId: grandParent.id, project: project1, relation: Relationship.IS_CHILD_OF.name).save(failOnError: true)
        new Link(ownerId: uncle.id, linkedId: parent.id, project: project1, relation: Relationship.IS_SIBLING_OF.name).save(failOnError: true)
        new Link(ownerId: parent.id, linkedId: uncle.id, project: project1, relation: Relationship.IS_SIBLING_OF.name).save(failOnError: true)

        new Step(act: "do something123", result: "something happened123", project: project,
                person: person, isBuilderStep: true, name: "builder step 1").save(failOnError: true)
        new Step(act: "do something123", result: "something happened123", project: project,
                person: person, isBuilderStep: true, name: "builder step 2").save(failOnError: true)
        new TestGroup(name: "Bootstrapped test group", project: project).save(failOnError: true)
        new TestGroup(name: "Bootstrapped test group1", project: project).save(failOnError: true)
        new TestGroup(name: "Bootstrapped test group2", project: project1).save(failOnError: true)
        new TestCase(person: person, name: "test part 1", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep, testStep1],
                project: project, area: area).save(failOnError: true)
        new TestCase(person: person, name: "test part 2", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep2],
                project: project1, area: area1).save(failOnError: true)
        new TestCase(person: person, name: "test part 3", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep3],
                project: project, area: area).save(failOnError: true)
        new TestCase(person: person, name: "test part 4", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep4],
                project: project1, area: area1, environments: [env1]).save(failOnError: true)
        def bugStep = new Step(act: "do something", result: "something happened", project: project1, person: person).save(failOnError: true)
        def bugStep1 = new Step(act: "do something", result: "something happened", project: project, person: person).save(failOnError: true)
        def bugStep2 = new Step(act: "do something", result: "something happened", project: project1, person: person).save(failOnError: true)
        def bugStep3 = new Step(act: "do something", result: "something happened", project: project1, person: person).save(failOnError: true)
        def bugStep4 = new Step(act: "do something", result: "something happened", project: project, person: person).save(failOnError: true)
        def bugStep5 = new Step(act: "do something", result: "something happened", project: project1, person: person).save(failOnError: true)
        new Bug(person: person, name: "seeded bug 1", description: "description of the bug",
                project: project1, steps: [bugStep], area: area1, status: "Open",
                actual: "actual", expected: "expected").save(failOnError: true)
        new Bug(person: person, name: "seeded bug 2", description: "description of the bug 1",
                project: project, steps: [bugStep1], area: area, status: "Open",
                actual: "actual", expected: "expected").save(failOnError: true)
        new Bug(person: person, name: "seeded bug 3", description: "description of the bug 2",
                project: project1, steps: [bugStep2, bugStep3], area: area1, status: "Open",
                actual: "actual", expected: "expected").save(failOnError: true)
        new Bug(person: person, name: "seeded bug 4", description: "description of the bug 1",
                project: project, steps: [bugStep4], area: area, status: "Open",
                actual: "actual", expected: "expected").save(failOnError: true)
        new Bug(person: person, name: "seeded bug 5", description: "description of the bug 2",
                project: project1, steps: [bugStep5], area: area1, status: "Open",
                actual: "actual", expected: "expected").save(failOnError: true)
        new Scenario(person: person, name: "scenario part 1", description: "desc",
                executionMethod: "Automated", type: "UI",
                project: project1).save(failOnError: true)
        new Scenario(person: person, name: "scenario part 2", description: "desc",
                executionMethod: "Automated", type: "UI",
                project: project).save(failOnError: true)
        def plan = new ReleasePlan(name: "Bootstrapped release plan", project: project, status: "ToDo", person: person).save(failOnError: true)
        new ReleasePlan(name: "Bootstrapped release plan1", project: project1, status: "ToDo", person: person).save(failOnError: true)
        def cycle = new TestCycle(name: "Bootstrapped test cycle")
        plan.addToTestCycles(cycle).save(failOnError: true)
    }
}
