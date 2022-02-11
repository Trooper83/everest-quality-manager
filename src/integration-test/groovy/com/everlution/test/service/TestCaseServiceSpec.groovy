package com.everlution.test.service

import com.everlution.Person
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.TestStepService
import com.everlution.command.RemovedItems
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestCaseServiceSpec extends Specification {

    ProjectService projectService
    TestCaseService testCaseService
    TestGroupService testGroupService
    TestStepService testStepService
    SessionFactory sessionFactory

    private Long setupData() {
        def person = new Person(email: "test1@test.com", password: "password").save()
        Project project = new Project(name: "TestCaseServiceSpec Project", code: "TTT").save()
        TestCase testCase = new TestCase(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project).save()
        new TestCase(person: person, name: "third", description: "desc3",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(person: person, name: "fourth", description: "desc4",
                executionMethod: "Manual", type: "UI", project: project).save()
        testCase.id
    }

    void "test get"() {
        def id = setupData()

        expect:
        testCaseService.get(id) != null
    }

    void "get returns null for not found id"() {
        expect:
        testCaseService.get(99999999) == null
    }

    void "get all returns list of tests"() {
        when:
        def tests = testCaseService.getAll([1,2,3])

        then:
        tests.size() == 3
    }

    void "get all with invalid id returns list of tests"() {
        when:
        def tests = testCaseService.getAll([1,9999999,3])

        then:
        tests.size() == 3
        tests.get(1) == null
        tests.get(0) != null
    }

    void "test list with no args"() {
        setupData()

        when:
        List<TestCase> testCaseList = testCaseService.list()

        then:
        testCaseList.size() > 0
    }

    void "test list with max args"() {
        setupData()

        when:
        List<TestCase> testCaseList = testCaseService.list(max: 1)

        then:
        testCaseList.size() == 1
    }

    void "test list with offset args"() {
        setupData()

        when:
        List<TestCase> testCaseList = testCaseService.list(offset: 1)

        then:
        testCaseList.size() > 0
    }

    void "test count"() {
        setupData()

        expect:
        testCaseService.count() > 1
    }

    void "test delete"() {
        Long testCaseId = setupData()

        given:
        def count = testCaseService.count()

        when:
        testCaseService.delete(testCaseId)
        sessionFactory.currentSession.flush()

        then:
        testCaseService.count() == count - 1
    }

    void "test save"() {
        when:
        def person = new Person(email: "test1@test.com", password: "password").save()
        Project project = new Project(name: "Test Case Save Project", code: "TCS").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project)
        testCaseService.save(testCase)

        then:
        testCase.id != null
    }

    void "save throws validation exception for failed validation"() {
        when:
        def person = new Person(email: "test1@test.com", password: "password").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API")
        testCaseService.save(testCase)

        then:
        thrown(ValidationException)
    }

    void "saveUpdate throws validation exception for failed validation"() {
        when:
        def person = new Person(email: "test1@test.com", password: "password").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API")
        testCaseService.save(testCase)

        then:
        thrown(ValidationException)
    }

    void "saveUpdate removes steps"() {
        given: "valid test case with step"
        def project = projectService.list(max: 1).first()
        def step = new Step(action: "action", result: "result")
        def person = new Person(email: "test1@test.com", password: "password").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project, steps: [step]).save()

        expect:
        testStepService.get(step.id) != null
        testCase.steps.size() == 1

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        testCaseService.saveUpdate(testCase, removed)

        then: "step is removed"
        testCase.steps.size() == 0
    }

    void "read returns instance"() {
        setup:
        def id = setupData()

        expect:
        testCaseService.read(id) instanceof TestCase
    }

    void "read returns null for not found id"() {
        expect:
        testCaseService.read(999999999) == null
    }

    void "delete test case with group"() {
        given:
        def project = projectService.list(max: 1).first()
        def group = new TestGroup(name: "test group", project: project).save()
        def person = new Person(email: "test1@test.com", password: "password").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project).save()
        testCase.addToTestGroups(group)

        expect:
        testCase.testGroups.size() == 1

        when:
        testCaseService.delete(testCase.id)
        sessionFactory.currentSession.flush()

        then:
        testCaseService.get(testCase.id) == null
        testGroupService.get(group.id) != null
    }
}
