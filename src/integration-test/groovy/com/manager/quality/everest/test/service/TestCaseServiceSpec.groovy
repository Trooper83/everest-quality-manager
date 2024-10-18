package com.manager.quality.everest.test.service

import com.manager.quality.everest.services.step.StepService
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.services.testgroup.TestGroupService
import com.manager.quality.everest.controllers.command.RemovedItems
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestCaseServiceSpec extends Specification {

    ProjectService projectService
    TestCaseService testCaseService
    TestGroupService testGroupService
    StepService stepService
    SessionFactory sessionFactory

    @Shared Project project
    @Shared Person person

    private Long setupData() {
        person = new Person(email: "test1@test.com", password: "!Password2022").save()
        project = new Project(name: "TestCaseServiceSpec Project", code: "TTT").save()
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

    void "test delete"() {
        setup:
        Long testCaseId = setupData()

        expect:
        TestCase.findById(testCaseId) != null

        when:
        testCaseService.delete(testCaseId)
        sessionFactory.currentSession.flush()

        then:
        TestCase.findById(testCaseId) == null
    }

    void "test save"() {
        when:
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        Project proj = new Project(name: "Test Case Save Project", code: "TCS").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: proj)
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

    void "saveUpdate removes free form steps"() {
        given: "valid test case with step"
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj, steps: [step]).save()

        expect:
        stepService.get(step.id) != null
        testCase.steps.size() == 1

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        testCaseService.saveUpdate(testCase, removed)

        then: "step is removed"
        testCase.steps.size() == 0
    }

    void "saveUpdate removes builder steps"() {
        given: "valid test case with step"
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result", isBuilderStep: true).save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj, steps: [step]).save()

        expect:
        stepService.get(step.id) != null
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
        def proj = projectService.list(max: 1).first()
        def group = new TestGroup(name: "test group", project: proj).save()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj).save()
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

    void "find all by project returns only test cases with project"() {
        given:
        setupData()
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "password").save()
        def tc = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj).save()

        when:
        def tests = testCaseService.findAllByProject(project, [:]).results

        then:
        tests.size() > 0
        tests.every { it.project.id == project.id }
        !tests.contains(tc)
    }

    void "find all by project with null project returns empty list"() {
        given:
        setupData()

        when:
        def tests = testCaseService.findAllByProject(null, [:])

        then:
        tests.results.size() == 0
        noExceptionThrown()
    }

    void "find all in project by name returns test cases"() {
        setup:
        setupData()

        expect:
        def tests = testCaseService.findAllInProjectByName(project, "first", [:])
        tests.results.first().name == "first"
    }

    void "delete method deletes free form steps"() {
        given: "valid test case with step"
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj, steps: [step]).save()

        expect:
        stepService.get(step.id) != null

        when: "call delete"
        testCaseService.delete(testCase.id)

        then: "step is removed"
        stepService.get(step.id) == null
    }

    void "delete method does delete builder steps"() {
        given: "valid test case with step"
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result", isBuilderStep: true).save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj, steps: [step]).save()

        expect:
        stepService.get(step.id) != null

        when: "call saveUpdate"
        testCaseService.delete(testCase.id)

        then: "step is removed"
        stepService.get(step.id) == null
    }

    void "saveUpdate method deletes free form steps"() {
        given: "valid test case with step"
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj, steps: [step]).save()

        expect:
        stepService.get(step.id) != null
        testCase.steps.size() == 1

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        testCaseService.saveUpdate(testCase, removed)
        sessionFactory.currentSession.flush()

        then: "step is removed"
        stepService.get(step.id) == null
    }

    void "saveUpdate method does not delete free form steps when validation fails"() {
        given: "valid test case with step"
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj, steps: [step]).save()

        expect:
        stepService.get(step.id) != null
        testCase.steps.size() == 1

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        testCase.name = ''
        testCaseService.saveUpdate(testCase, removed)

        then: "step is removed"
        thrown(ValidationException)
        stepService.get(step.id) != null
    }

    void "saveUpdate method does not delete builder steps"() {
        given: "valid test case with step"
        def proj = projectService.list(max: 1).first()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def step = new Step(act: "action", result: "result", isBuilderStep: true).save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: proj, steps: [step]).save()

        expect:
        stepService.get(step.id) != null
        testCase.steps.size() == 1

        when: "call saveUpdate"
        def removed = new RemovedItems()
        removed.stepIds = [step.id]
        testCaseService.saveUpdate(testCase, removed)

        then: "step is not deleted"
        stepService.get(step.id) != null
    }

    void "getAllByGroup returns test cases in group"() {
        given:
        setupData()
        def group = new TestGroup(name: "name 12345", project: project).save()
        project.addToTestGroups(group)
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project).save()
        group.addToTestCases(testCase)
        testCase.addToTestGroups(group)
        sessionFactory.currentSession.flush()

        when:
        def tests = testCaseService.getAllByGroup(group.id, [:])

        then:
        tests.first() == testCase
    }
}
