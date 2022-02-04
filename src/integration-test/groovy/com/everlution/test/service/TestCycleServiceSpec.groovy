package com.everlution.test.service

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestCycleService
import com.everlution.TestIteration
import com.everlution.TestIterationService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestCycleServiceSpec extends Specification {

    TestCycleService testCycleService
    TestIterationService testIterationService
    SessionFactory sessionFactory

    private Long setupData() {
        def project = new Project(name: "release name765", code: "glo").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        new TestCycle(name: "First Test Case", releasePlan: plan).save()
        new TestCycle(name: "Second Test Case", releasePlan: plan).save(flush: true).id
    }

    void "test get"() {
        def id = setupData()

        expect:
        testCycleService.get(id) != null
    }

    void "test save"() {
        when:
        def project = new Project(name: "release project name", code: "rpn").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        def testCycle = new TestCycle(name: "Second Test Case", releasePlan: plan)
        testCycleService.save(testCycle)

        then:
        testCycle.id != null
    }

    void "removeFrom cycle deletes iteration"() {
        given:
        def project = new Project(name: "release project name", code: "rpn").save()
        def plan = new ReleasePlan(name: "release plan name", project: project).save()
        def person = new Person(email: "test@test.com", password: "test").save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: []).save()
        TestCycle cycle = new TestCycle(name: "test cycle", releasePlan: plan, testIterations: [iteration])
        testCycleService.save(cycle)

        expect:
        testCycleService.get(cycle.id) != null
        testIterationService.get(iteration.id) != null

        when: "remove iteration"
        testCycleService.removeTestIteration(cycle, iteration)
        sessionFactory.currentSession.flush()

        then: "iteration deleted"
        testIterationService.get(iteration.id) == null
    }
}
