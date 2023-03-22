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
            integrated {
                // intentionally blank
            }
            production {
                // intentionally blank
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

        def readOnlyUser = new Person(email: "read_only@readonly.com", password: "!Password#2022").save(failOnError: true)
        def basicUser = new Person(email: "basic@basic.com", password: "!Password#2022").save(failOnError: true)
        def projectAdminUser = new Person(email: "project_admin@projectadmin.com", password: "!Password#2022").save(failOnError: true)
        def orgAdminUser = new Person(email: "org_admin@orgadmin.com", password: "!Password#2022").save(failOnError: true)
        def appAdminUser = new Person(email: "app_admin@appadmin.com", password: "!Password#2022").save(failOnError: true)
        new Person(email: "expired@accountexpired.com", password: "!Password#2022", accountExpired: true).save(failOnError: true)
        new Person(email: "locked@accountlocked.com", password: "!Password#2022", accountLocked: true).save(failOnError: true)
        new Person(email: "expired@passwordexpired.com", password: "!Password#2022", passwordExpired: true).save(failOnError: true)
        new Person(email: "disabled@disabled.com", password: "!Password#2022", enabled: false).save(failOnError: true)

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
        def person = new Person(email: "test@bootstrapped.com", password: "!Password#2022").save()
        def area = new Area(name: "bootstrap area")
        def area1 = new Area(name: "bootstrap area 12")
        def env = new Environment(name: "bootstrap environment")
        def env1 = new Environment(name: "bootstrap environment12")
        def project = new Project(name: "bootstrap project", code: "bsp", areas: [area], environments: [env]).save()
        def project1 = new Project(name: "bootstrap project12", code: "BPP", areas: [area1], environments: [env1]).save()
        Step testStep = new Step(action: "do something", result: "something happened").save(failOnError: true)
        Step testStep1 = new Step(action: "do something12", result: "something happened12").save(failOnError: true)
        Step testStep2 = new Step(action: "do something123", result: "something happened123").save(failOnError: true)
        Step testStep3 = new Step(action: "do something123", result: "something happened123").save(failOnError: true)
        Step testStep4 = new Step(action: "do something123", result: "something happened123").save(failOnError: true)
        def group = new TestGroup(name: "Bootstrapped test group", project: project).save(failOnError: true)
        def group1 = new TestGroup(name: "Bootstrapped test group1", project: project).save(failOnError: true)
        def group2 = new TestGroup(name: "Bootstrapped test group2", project: project1).save(failOnError: true)
        def testCase = new TestCase(person: person, name: "test part 1", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep, testStep1],
                project: project, area: area).save(failOnError: true)
        def testCase1 = new TestCase(person: person, name: "test part 2", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep2],
                project: project1, area: area1).save(failOnError: true)
        def testCase2 = new TestCase(person: person, name: "test part 3", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep3],
                project: project, area: area).save(failOnError: true)
        def testCase3 = new TestCase(person: person, name: "test part 4", description: "desc",
                executionMethod: "Automated", type: "UI", steps: [testStep4],
                project: project1, area: area1).save(failOnError: true)
        testCase.addToTestGroups(group)
        testCase.addToTestGroups(group1)
        testCase1.addToTestGroups(group2)
        testCase2.addToTestGroups(group)
        testCase3.addToTestGroups(group2)
        def bugStep = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep1 = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep2 = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep3 = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep4 = new Step(action: "do something", result: "something happened").save(failOnError: true)
        def bugStep5 = new Step(action: "do something", result: "something happened").save(failOnError: true)
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
        def plan = new ReleasePlan(name: "Bootstrapped release plan", project: project, status: "ToDo").save(failOnError: true)
        new ReleasePlan(name: "Bootstrapped release plan1", project: project1, status: "ToDo").save(failOnError: true)
        def cycle = new TestCycle(name: "Bootstrapped test cycle")
        plan.addToTestCycles(cycle).save(failOnError: true)

        new Project(name: "bootstrap projecteqwr", code: "gfd", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap project12wqerf", code: "hgh", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap projectfsfasd", code: "hgj", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap project12sdafsdaf", code: "jhg", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap projectxvccxv", code: "bcx", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap project12xvcxv", code: "utr", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap projectbffdsb", code: "kgf", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap projedfbdct12", code: "iyr", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap prdfsgadsfoject", code: "pwq", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap prfsbdfsbfoject12", code: "jui", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap projeafFct", code: "oiy", areas: [area], environments: [env]).save()
        new Project(name: "bootsfSstrap project12", code: "pty", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrSFSADFSFap project", code: "nbg", areas: [area], environments: [env]).save()
        new Project(name: "bootstSAFSADFrap project12", code: "mkj", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap progghghject", code: "wer", areas: [area], environments: [env]).save()
        new Project(name: "bootshgjhgjhgjtrap project12", code: "hdf", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap prgfhjgfhjoject", code: "hfd", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap projecghfghjjt12", code: "csa", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap fghjfgjproject", code: "kut", areas: [area], environments: [env]).save()
        new Project(name: "bootstrapghjfghj project12", code: "bgd", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrafgtrretp project", code: "kut", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap projerttwertct12", code: "wer", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap proj3565436ect", code: "bvc", areas: [area], environments: [env]).save()
        new Project(name: "bootstra3456456p project12", code: "zzz", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap projfhjhect", code: "vcx", areas: [area], environments: [env]).save()
        new Project(name: "bootsghjhgjtrap project12", code: "bbb", areas: [area1], environments: [env1]).save()
        new Project(name: "bootjfhgjstrap project", code: "nnn", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap proghjgfjject12", code: "mmm", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap proshfgject", code: "lll", areas: [area], environments: [env]).save()
        new Project(name: "bootgfdsgsfdgstrap project12", code: "jjj", areas: [area1], environments: [env1]).save()
        new Project(name: "bootssdgtrap prdfgoject", code: "hhh", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap prodgdsgject12", code: "ggg", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstfgsdfgrap project", code: "fff", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap prosdfgdsfgject12", code: "ddd", areas: [area1], environments: [env1]).save()
        new Project(name: "boodgfdfgtstrap project", code: "sss", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap prodgfject12", code: "aaa", areas: [area1], environments: [env1]).save()
        new Project(name: "bootghgfhfghstrap project", code: "qqq", areas: [area], environments: [env]).save()
        new Project(name: "bootsfghfghtrap project12", code: "www", areas: [area1], environments: [env1]).save()
        new Project(name: "boovbnvbntstrap projnvbvnect", code: "eee", areas: [area], environments: [env]).save()
        new Project(name: "bootsvbnvbtrap project12", code: "eer", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap projenvbnbvnct", code: "rrr", areas: [area], environments: [env]).save()
        new Project(name: "bootstvbnbvnrap project12", code: "ttt", areas: [area1], environments: [env1]).save()
        new Project(name: "bootvbnvbnstrap project", code: "yyy", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap projebnvnct12", code: "uuu", areas: [area1], environments: [env1]).save()
        new Project(name: "bootvnbvbnstrap project", code: "iii", areas: [area], environments: [env]).save()
        new Project(name: "boobvnvbntstrap project12", code: "ooo", areas: [area1], environments: [env1]).save()
        new Project(name: "bootsvbnvbtrap project", code: "ppp", areas: [area], environments: [env]).save()
        new Project(name: "bootstrnap projecnfnfgnft12", code: "gee", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap 234", code: "lkj", areas: [area1], environments: [env1]).save()
        new Project(name: "boodgfdfgtstrap erwer", code: "jhg", areas: [area], environments: [env]).save()
        new Project(name: "bootstrap wer", code: "gfd", areas: [area1], environments: [env1]).save()
        new Project(name: "sdf project", code: "fds", areas: [area], environments: [env]).save()
        new Project(name: "gfdfg project12", code: "asd", areas: [area1], environments: [env1]).save()
        new Project(name: "boovbnvbntstrap gdfg", code: "qwe", areas: [area], environments: [env]).save()
        new Project(name: "cxvcxv project12", code: "wer", areas: [area1], environments: [env1]).save()
        new Project(name: "bootstrap vxcv", code: "ert", areas: [area], environments: [env]).save()
        new Project(name: "xv project12", code: "tyu", areas: [area1], environments: [env1]).save()
        new Project(name: "bootvbnvbnstrap cv", code: "uio", areas: [area], environments: [env]).save()
        new Project(name: "xcv projebnvnct12", code: "cvb", areas: [area1], environments: [env1]).save()
        new Project(name: "bootvnbvbnstrap vcxv", code: "mnb", areas: [area], environments: [env]).save()
        new Project(name: "boobvnvbntstrap pxcvsdroject12", code: "zxf", areas: [area1], environments: [env1]).save()
        new Project(name: "tert project", code: "sry", areas: [area], environments: [env]).save()
        new Project(name: "trtrr projecnfnfgnft12", code: "ijn", areas: [area1], environments: [env1]).save()
    }
}
