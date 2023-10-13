package com.everlution.test.testcycle

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestIteration
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class TestCycleHibernateSpec extends HibernateSpec {

    @Shared Person person
    @Shared Project project
    @Shared ReleasePlan releasePlan
    @Shared TestCase testCase

    def setup() {
        project = new Project(name: "Test Case Date Project For Cycle", code: "TCC").save()
        person = new Person(email: "test@test.com", password: "!Password2022").save()
        releasePlan = new ReleasePlan(name: "this is the name", project: project, status: "ToDo",
                person: person).save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "test date created auto generated"() {
        when:
        def cycle = new TestCycle(name: "testing")
        releasePlan.addToTestCycles(cycle).save(flush: true)

        then:
        cycle.dateCreated != null
    }

    void "save cycle does not cascade to release plan"() {
        given: "unsaved release plan"
        def plan = new ReleasePlan(name: "this is the name", project: project)

        when: "save test cycle"
        new TestCycle(name: "test cycle", releasePlan: plan).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete cycle does not cascade to release plan"() {
        given: "saved release plan & cycle"
        TestCycle cycle = new TestCycle(name: "test cycle", releasePlan: releasePlan).save()

        expect:
        TestCycle.findById(cycle.id) != null
        ReleasePlan.findById(releasePlan.id) != null

        when: "delete test cycle"
        cycle.delete(flush: true)

        then: "cycle deleted & plan is not"
        TestCycle.findById(cycle.id) == null
        ReleasePlan.findById(releasePlan.id) != null
    }

    void "delete cycle with iteration throws exception"() {
        given:
        TestCycle cycle = new TestCycle(name: "test cycle", releasePlan: releasePlan).save()
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [], testCycle: cycle).save()

        expect:
        TestCycle.findById(cycle.id) != null
        TestIteration.findById(iteration.id) != null

        when: "delete test cycle"
        cycle.delete(flush: true)

        then:
        thrown(DataIntegrityViolationException)
    }

    void "removeFrom cycle with iteration throws exception"() {
        given:
        TestCycle cycle = new TestCycle(name: "test cycle", releasePlan: releasePlan).save()
        def iteration = new TestIteration(name: "test name", testCase: testCase, result: "ToDo", steps: [], testCycle: cycle).save()

        expect:
        TestCycle.findById(cycle.id) != null
        TestIteration.findById(iteration.id) != null

        when: "delete test cycle"
        cycle.removeFromTestIterations(iteration).save(flush: true)

        then:
        thrown(DataIntegrityViolationException)
    }
}
