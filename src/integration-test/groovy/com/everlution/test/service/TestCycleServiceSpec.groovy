package com.everlution.test.service

import com.everlution.Environment
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestCycleService
import com.everlution.TestIteration
import com.everlution.test.support.DataFactory
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestCycleServiceSpec extends Specification {

    PersonService personService
    ReleasePlanService releasePlanService
    TestCycleService testCycleService
    SessionFactory sessionFactory

    @Shared Person person

    private Long setupData() {
        person = personService.list([max:1]).first()
        def project = new Project(name: "release name765", code: "glo").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        new TestCycle(name: "First Test Case", releasePlan: plan).save()
        new TestCycle(name: "Second Test Case", releasePlan: plan).save(flush: true).id
    }

    void "test get"() {
        def id = setupData()

        expect:
        testCycleService.get(id) != null
    }

    void "removeFrom cycle deletes iteration"() {
        given:
        def project = new Project(name: "release project name", code: "rpn").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        TestCycle cycle = new TestCycle(name: "test cycle")
        releasePlanService.addTestCycle(plan, cycle)
        testCycleService.addTestIterations(cycle, [testCase])
        sessionFactory.currentSession.flush()
        def iteration = cycle.testIterations.first()

        expect:
        testCycleService.get(cycle.id) != null
        TestIteration.findById(iteration.id) != null

        when: "remove iteration"
        testCycleService.removeTestIteration(cycle, iteration)
        sessionFactory.currentSession.flush()

        then: "iteration deleted"
        TestIteration.findById(iteration.id) == null
    }

    void "add iterations persists iterations"() {
        given:
        def project = new Project(name: "release project name", code: "rpn").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        TestCycle cycle = new TestCycle(name: "test cycle")
        releasePlanService.addTestCycle(plan, cycle)

        when: "add iteration"
        testCycleService.addTestIterations(cycle, [testCase])
        sessionFactory.currentSession.flush()

        then: "iteration saved"
        cycle.testIterations.size() == 1
    }

    void "add test iterations does not filter test cases when platform is null"() {
        when:
        def project = DataFactory.createProject()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "").save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "iOS").save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Android").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan, platform: "").save(flush: true)
        testCycleService.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 4
    }

    void "add test iterations filters out test cases by platform"() {
        when:
        def project = DataFactory.createProject()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "").save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "iOS").save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Android").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan, platform: "Web").save(flush: true)
        testCycleService.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 2
    }

    void "add test iterations does not filter test cases when environ is null"() {
        when:
        def project = DataFactory.createProject()
        def env1 = new Environment(name: "env1")
        def env2 = new Environment(name: "env2")
        project.addToEnvironments(env1).addToEnvironments(env2).save()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1]).save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1, env2]).save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env2]).save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project).save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan).save(flush: true)
        testCycleService.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 4
    }

    void "add test iterations filters out test cases by environ"() {
        when:
        def project = DataFactory.createProject()
        def env1 = new Environment(name: "env1")
        def env2 = new Environment(name: "env2")
        project.addToEnvironments(env1).addToEnvironments(env2).save()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1]).save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1, env2]).save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env2]).save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project).save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan, environ: env1).save(flush: true)
        testCycleService.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 3
    }

    void "add test iterations filters out test cases by already on test cycle"() {
        given:
        def project = DataFactory.createProject()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan, platform: "Web", testCaseIds: [testCase.id]).save(flush: true)

        when:
        testCycleService.addTestIterations(testCycleService.get(tc.id), [testCase])

        then:
        !tc.testIterations
    }

    void "add test iterations adds test cases ids to test cycle"() {
        given:
        def project = DataFactory.createProject()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan, platform: "Web", testCaseIds: [testCase.id]).save(flush: true)

        expect:
        def cycle = testCycleService.get(tc.id)
        cycle.testCaseIds.size() == 1

        when:
        testCycleService.addTestIterations(cycle, [testCase, testCase1])

        then:
        testCycleService.get(tc.id).testCaseIds.size() == 2
    }

    void "getWithPaginatedTests returns group and tests"() {
        given:
        setupData()
        def project = DataFactory.createProject()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        def plan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo", person: person).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: plan, platform: "Web", testCaseIds: []).save(flush: true)
        testCycleService.addTestIterations(tc, [testCase])

        when:
        def result = testCycleService.getWithPaginatedTests(tc.id, [:])

        then:
        result.testCycle == tc
        result.iterations.first().testCase.id == testCase.id
    }
}
