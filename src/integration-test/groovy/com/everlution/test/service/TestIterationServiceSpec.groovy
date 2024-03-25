package com.everlution.test.service

import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.domains.ReleasePlan
import com.everlution.domains.TestCase
import com.everlution.domains.TestCycle
import com.everlution.domains.TestIteration
import com.everlution.services.testiteration.TestIterationService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import grails.validation.ValidationException
import spock.lang.Specification

@Integration
@Rollback
class TestIterationServiceSpec extends Specification {

    PersonService personService
    ProjectService projectService
    TestIterationService testIterationService

    TestIteration setupData() {
        def person = personService.list(max: 1).first()
        def project = projectService.list(max: 1).first()
        def testCase = new TestCase(name: "name of test case", project: project, person: person).save()
        def plan = new ReleasePlan(name: "plan", project: project, status: "ToDo", person: person).save()
        def cycle = new TestCycle(name: "name of cycle", releasePlan: plan).save()
        new TestIteration(name: "name of test iteration", testCase: testCase, result: "ToDo", steps: [],
                testCycle: cycle)
    }

    void "test get"() {
        def iteration = setupData().save()

        expect:
        testIterationService.get(iteration.id) != null
    }

    void "read returns instance"() {
        setup:
        def iteration = setupData().save()

        expect:
        testIterationService.read(iteration.id) instanceof TestIteration
    }

    void "test save"() {
        given:
        def iteration = setupData()

        when:
        testIterationService.save(iteration)

        then:
        iteration.id != null
    }

    void "save throws exception with validation fail"() {
        when:
        testIterationService.save(new TestIteration())

        then:
        thrown(ValidationException)
    }

    void "read returns null for not found id"() {
        expect:
        testIterationService.read(999999999) == null
    }

    void "findAllByTestCycle returns iterations"() {
        given:
        def person = personService.list(max: 1).first()
        def project = projectService.list(max: 1).first()
        def testCase = new TestCase(name: "name of test case", project: project, person: person).save()
        def plan = new ReleasePlan(name: "plan", project: project, status: "ToDo", person: person).save()
        def cycle = new TestCycle(name: "name of cycle", releasePlan: plan).save()
        def iteration = new TestIteration(name: "name of test iteration", testCase: testCase, result: "ToDo", steps: [],
                testCycle: cycle)
        testIterationService.save(iteration)
        cycle.addToTestIterations(iteration)

        when:
        def found = testIterationService.findAllByTestCycle(cycle, [:])

        then:
        found.size() == 1
        found.first() instanceof TestIteration
    }
}
