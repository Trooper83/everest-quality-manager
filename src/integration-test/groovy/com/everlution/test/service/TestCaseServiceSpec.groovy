package com.everlution.test.service

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestStepService
import com.everlution.command.RemovedItems
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestCaseServiceSpec extends Specification {

    ProjectService projectService
    TestCaseService testCaseService
    TestStepService testStepService
    SessionFactory sessionFactory

    private Long setupData() {
        Project project = new Project(name: "TestCaseServiceSpec Project", code: "TTT").save()
        TestCase testCase = new TestCase(creator: "test",name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(creator: "test",name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project).save()
        new TestCase(creator: "test",name: "third", description: "desc3",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(creator: "test",name: "fourth", description: "desc4",
                executionMethod: "Manual", type: "UI", project: project).save()
        testCase.id
    }

    void "test get"() {
        def id = setupData()

        expect:
        testCaseService.get(id) != null
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
        Project project = new Project(name: "Test Case Save Project", code: "TCS").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project)
        testCaseService.save(testCase)

        then:
        testCase.id != null
    }

    void "test delete all by project"() {
        Long caseId = setupData()

        given:
        def project = testCaseService.get(caseId).project

        expect:
        TestCase.findAllByProject(project).size() == 4

        when:
        testCaseService.deleteAllTestCasesByProject(project)
        sessionFactory.currentSession.flush()

        then:
        TestCase.findAllByProject(project).size() == 0
    }

    void "saveUpdate removes steps"() {
        given: "valid test case with step"
        def project = projectService.list(max: 1).first()
        def step = new Step(action: "action", result: "result")
        def testCase = new TestCase(creator: "test", name: "second", description: "desc2",
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
}
