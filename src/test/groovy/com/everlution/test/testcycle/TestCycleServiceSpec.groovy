package com.everlution.test.testcycle

import com.everlution.domains.Environment
import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.ReleasePlan
import com.everlution.domains.Step
import com.everlution.domains.TestCase
import com.everlution.domains.TestCycle
import com.everlution.services.testcycle.TestCycleService
import com.everlution.domains.TestIteration
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestCycleServiceSpec extends Specification implements ServiceUnitTest<TestCycleService>, DataTest {

    @Shared ReleasePlan releasePlan
    @Shared Person person
    @Shared Project project

    def setupSpec() {
        mockDomains(Project, ReleasePlan, TestCycle)
    }

    def setup() {
        project = new Project(name: "release name", code: "doc").save()
        person = new Person(email: "test@test.com", password: "test").save()
        releasePlan = new ReleasePlan(name: "release plan name", project: project, status: "ToDo",
                person: person).save()
    }

    private void setupData() {
        new TestCycle(name: "First Test Case", releasePlan: releasePlan).save()
        new TestCycle(name: "Second Test Case", releasePlan: releasePlan).save(flush: true)
    }

    void "get returns valid instance"() {
        setupData()

        expect:
        service.get(1) instanceof TestCycle
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "get with null returns null"() {
        expect:
        service.get(null) == null
    }

    void "removeFrom removes iterations"() {
        given:
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "iteration", testCase: testCase, steps: [], result: "ToDo")
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan).addToTestIterations(iteration).save(flush: true)

        expect:
        tc.testIterations.size() == 1

        when:
        service.removeTestIteration(tc, iteration)

        then:
        tc.testIterations.empty
    }

    void "add test iterations populates property on instance"() {
        given:
        def step = new Step(act: "action", result: "result")
        def step1 = new Step(act: "action1", result: "result1", data: "data1")
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project, steps: [step, step1], verify: "verified").save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan).save(flush: true)

        expect:
        tc.id != null
        tc.testIterations == null

        when:
        service.addTestIterations(tc, [testCase])

        then:
        tc.testIterations.size() == 1
        tc.testIterations.first().name == testCase.name
        tc.testIterations.first().verify == testCase.verify
        tc.testIterations.first().result == "ToDo"
        tc.testIterations.first().testCase == testCase
        tc.testIterations.first().steps.size() == testCase.steps.size()
        tc.testIterations.first().steps[0].act == testCase.steps[0].act
        tc.testIterations.first().steps[1].act == testCase.steps[1].act
        tc.testIterations.first().steps[0].result == testCase.steps[0].result
        tc.testIterations.first().steps[1].result == testCase.steps[1].result
        tc.testIterations.first().steps[0].data == null
        tc.testIterations.first().steps[1].data == testCase.steps[1].data
    }

    void "add iterations from test cases returns same number of tests cases"() {
        when:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project).save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan).save(flush: true)
        service.addTestIterations(tc, [testCase, testCase1])

        then:
        tc.testIterations.size() == 2
    }

    void "add iterations with empty test case list does not throw exception"() {
        when:
        service.addTestIterations(new TestCycle(), [])

        then:
        noExceptionThrown()
    }

    void "add iterations with null test case list does not throw exception"() {
        when:
        service.addTestIterations(new TestCycle(), null)

        then:
        noExceptionThrown()
    }

    void "add iterations with null steps on test case does not throw exception"() {
        given:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project).save()

        expect:
        testCase.steps == null

        when:
        service.addTestIterations(new TestCycle(), [testCase])

        then:
        noExceptionThrown()
    }

    void "add test iterations does not filter test cases when platform is null"() {
        when:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "").save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "iOS").save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Android").save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan, platform: "").save(flush: true)
        service.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 4
    }

    void "add test iterations filters out test cases by platform"() {
        when:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "").save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "iOS").save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Android").save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan, platform: "Web").save(flush: true)
        service.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 2
    }

    void "add test iterations does not filter test cases when environ is null"() {
        when:
        def env1 = new Environment(name: "env1")
        def env2 = new Environment(name: "env2")
        project.addToEnvironments(env1).addToEnvironments(env2).save()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1]).save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1, env2]).save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env2]).save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan).save(flush: true)
        service.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 4
    }

    void "add test iterations filters out test cases by environ"() {
        when:
        def env1 = new Environment(name: "env1")
        def env2 = new Environment(name: "env2")
        project.addToEnvironments(env1).addToEnvironments(env2).save()
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1]).save()
        TestCase testCase1 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env1, env2]).save()
        TestCase testCase11 = new TestCase(person: person, name: "Second Test Case", project: project, environments: [env2]).save()
        TestCase testCase111 = new TestCase(person: person, name: "Second Test Case", project: project).save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan, environ: env1).save(flush: true)
        service.addTestIterations(tc, [testCase, testCase1, testCase11, testCase111])

        then:
        tc.testIterations.size() == 3
    }

    void "add test iterations filters out test cases by unique"() {
        when:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan, platform: "Web").save(flush: true)
        service.addTestIterations(tc, [testCase, testCase, testCase])

        then:
        tc.testIterations.size() == 1
    }

    void "add test iterations filters out test cases by already on test cycle"() {
        when:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan, platform: "Web", testCaseIds: [testCase.id]).save(flush: true)
        service.addTestIterations(tc, [testCase])

        then:
        !tc.testIterations
    }

    void "add test iterations adds test case ids to test cycle with null test case ids"() {
        given:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan, platform: "Web").save(flush: true)

        expect:
        tc.testCaseIds == null

        when:
        service.addTestIterations(tc, [testCase])

        then:
        tc.testCaseIds.size() == 1
    }

    void "add test iterations adds test case ids to test cycle with existing test case ids"() {
        given:
        TestCase testCase = new TestCase(person: person, name: "Second Test Case", project: project, platform: "Web").save()
        TestCycle tc = new TestCycle(name: "First Test Case", releasePlan: releasePlan, platform: "Web", testCaseIds: [9999]).save(flush: true)

        expect:
        tc.testCaseIds.size() == 1

        when:
        service.addTestIterations(tc, [testCase])

        then:
        tc.testCaseIds.size() == 2
    }
}
