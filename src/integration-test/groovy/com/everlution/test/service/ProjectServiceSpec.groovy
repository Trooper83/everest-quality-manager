package com.everlution.test.service

import com.everlution.Bug
import com.everlution.BugService
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

    BugService bugService
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

    void "test list with no args"() {
        setupData()

        when:
        List<Project> projectList = projectService.list()

        then:
        projectList.size() == 5
    }

    void "test list with max args"() {
        setupData()

        when:
        List<Project> projectList = projectService.list(max: 1)

        then:
        projectList.size() == 1
    }

    void "test list with offset args"() {
        setupData()

        when:
        List<Project> projectList = projectService.list(offset: 1)

        then:
        projectList.size() == 4
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

        then: "test case and steps deleted"
        projectService.get(project.id) == null
        testCaseService.get(testCase.id) == null
        testStepService.get(step.id) == null
    }

    void "delete project cascades to bug"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        Bug bug = new Bug(name: "cascade project", description: "this should delete", creator: "testing",
                project: project).save()

        expect:
        project.id != null
        bug.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "bug and project are deleted"
        projectService.get(project.id) == null
        bugService.get(bug.id) == null
    }
}
