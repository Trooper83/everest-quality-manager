package com.everlution.test.service

import com.everlution.TestStepService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestStep
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ProjectServiceSpec extends Specification {

    ProjectService projectService
    SessionFactory sessionFactory
    TestCaseService testCaseService
    TestStepService testStepService

    private Long setupData() {
        new Project(name: "project service 1", code: "ZZZ").save()
        new Project(name: "project service 2", code: "ZZA").save()
        def project = new Project(name: "project service 3", code: "ZZB").save()
        project.id
    }

    void "test get"() {
        setupData()

        expect:
        projectService.get(1) != null
    }

    void "test list"() {
        setupData()

        when:
        List<Project> projectList = projectService.list(max: 2, offset: 2)

        then:
        projectList.size() == 2
    }

    void "test count"() {
        setupData()

        expect:
        projectService.count() > 1
    }

    void "test delete"() {
        Long projectId = setupData()

        given:
        def c = projectService.count()

        when:
        projectService.delete(projectId)
        sessionFactory.currentSession.flush()

        then:
        projectService.count() == c - 1
    }

    void "test save"() {
        when:
        Project project = new Project(name: "testing save project", code: "SSZ")
        projectService.save(project)

        then:
        project.id != null
    }

    void "delete project cascades to test case and steps"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
        TestStep step = new TestStep(action: "first action", result: "first result").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project, steps: [step]).save()

        expect:
        project.id != null
        testCase.id != null
        step.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "test case is deleted"
        projectService.get(project.id) == null
        testCaseService.get(testCase.id) == null
        testStepService.get(step.id) == null
    }

    void "delete test case does not cascade to project"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
        TestStep step = new TestStep(action: "first action", result: "first result").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project, steps: [step]).save()

        when: "delete test case"
        testCaseService.delete(testCase.id)
        sessionFactory.currentSession.flush()

        then: "project is not deleted"
        testCaseService.get(testCase.id) == null
        projectService.get(project.id) != null
    }
}
