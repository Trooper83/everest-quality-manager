package com.manager.quality.everest.test.unit.testcycle

import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.domains.TestIteration
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class TestCycleHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Project, Platform, Environment, ReleasePlan, Person] }

    @Shared Environment environment
    @Shared Platform platform
    @Shared Person person
    @Shared Project project
    @Shared ReleasePlan releasePlan
    @Shared TestCase testCase

    def setup() {
        platform = new Platform(name: 'platform name')
        environment = new Environment(name: 'env name')
        project = new Project(name: "Test Case Date Project For Cycle", code: "TCC", platforms: [platform], environments: [environment]).save()
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
        def iteration = new TestIteration(name: "test name", testCase: testCase, steps: [], testCycle: cycle).save()

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
        def iteration = new TestIteration(name: "test name", testCase: testCase, steps: [], testCycle: cycle).save()

        expect:
        TestCycle.findById(cycle.id) != null
        TestIteration.findById(iteration.id) != null

        when: "delete test cycle"
        cycle.removeFromTestIterations(iteration).save(flush: true)

        then:
        thrown(DataIntegrityViolationException)
    }

    void "delete does not cascade to platform"() {
        given:
        TestCycle cycle = new TestCycle(name: "test cycle for platform cascade", releasePlan: releasePlan, platform: platform).save()

        expect:
        cycle.id != null

        when:
        cycle.delete(flush: true)

        then:
        TestCycle.findById(cycle.id) == null
        Platform.findById(platform.id) != null
    }

    void "delete does not cascade to environment"() {
        given:
        TestCycle cycle = new TestCycle(name: "test cycle for platform cascade", releasePlan: releasePlan, environ: environment).save()

        expect:
        cycle.id != null

        when:
        cycle.delete(flush: true)

        then:
        TestCycle.findById(cycle.id) == null
        Environment.findById(environment.id) != null
    }
}
