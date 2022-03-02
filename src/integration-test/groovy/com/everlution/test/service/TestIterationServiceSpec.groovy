package com.everlution.test.service

import com.everlution.Bug
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.TestCase
import com.everlution.TestCycle
import com.everlution.TestIteration
import com.everlution.TestIterationService
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

    void "test get"() {
        def person = personService.list(max: 1).first()
        def project = projectService.list(max: 1).first()
        def testCase = new TestCase(name: "name of test case", project: project, person: person)
        def plan = new ReleasePlan(name: "plan", project: project).save()
        def cycle = new TestCycle(name: "name of cycle", releasePlan: plan).save()
        def iteration = new TestIteration(name: "name of test iteration", testCase: testCase, result: "ToDo", steps: [],
            testCycle: cycle).save()

        expect:
        testIterationService.get(iteration.id) != null
    }
    //TODO: update these tests for test iteration
/*
    void "test save"() {
        when:
        def person = new Person(email: "test988@test.com", password: "password").save()
        Project project = new Project(name: "BugServiceSpec Project", code: "BPM").save()
        Bug bug = new Bug(person: person, description: "Found a bug123", name: "Name of the bug123", project: project)
        bugService.save(bug)

        then:
        bug.id != null
    }

    void "save throws exception with validation fail"() {
        when:
        def person = new Person(email: "test@test.com", password: "password").save()
        Bug bug = new Bug(person: person, description: "Found a bug123", name: "Name of the bug123")
        bugService.save(bug)

        then:
        thrown(ValidationException)
    }

    void "read returns instance"() {
        setup:
        def id = setupData()

        expect:
        bugService.read(id) instanceof Bug
    }

    void "read returns null for not found id"() {
        expect:
        bugService.read(999999999) == null
    }
 */
}
