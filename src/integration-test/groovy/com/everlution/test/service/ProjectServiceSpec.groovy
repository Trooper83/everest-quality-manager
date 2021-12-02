package com.everlution.test.service

import com.everlution.Area
import com.everlution.AreaService
import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Environment
import com.everlution.EnvironmentService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.command.RemovedItems
import com.everlution.test.support.DataFactory
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ProjectServiceSpec extends Specification {

    AreaService areaService
    BugService bugService
    EnvironmentService environmentService
    ProjectService projectService
    SessionFactory sessionFactory
    ScenarioService scenarioService
    TestCaseService testCaseService

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
        projectList.size() > 0
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
        projectList.size().toLong() == projectService.count() - 1
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

    void "delete project removes all scenarios"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZD").save()
        def scenario = new Scenario(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        expect:
        project.id != null
        scenario.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "scenario deleted"
        projectService.get(project.id) == null
        scenarioService.get(scenario.id) == null
    }

    void "delete project removes all test cases"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        expect:
        project.id != null
        testCase.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "test case and steps deleted"
        projectService.get(project.id) == null
        testCaseService.get(testCase.id) == null
    }

    void "delete project removes all bugs"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
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

    void "delete project removes all associated areas"() {
        given:
        def ad = DataFactory.area()
        Area area = new Area(ad)
        def pd = DataFactory.project()
        Project project = new Project(name: pd.name, code: pd.code, areas: [area]).save()

        expect:
        project.id != null
        area.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "area and project are deleted"
        projectService.get(project.id) == null
        areaService.get(area.id) == null
    }

    void "saveUpdate removes area"() {
        given: "project with area"
        def area = new Area(name: "area name").save()
        Project project = new Project(name: "Remove Area Test", code: "RAT").addToAreas(area).save()

        expect:
        project.areas.size() == 1
        area.id != null

        when: "call saveUpdate"
        def items = new RemovedItems()
        items.areaIds = [area.id]
        projectService.saveUpdate(project, items)
        sessionFactory.currentSession.flush()

        then: "area is removed"
        project.areas.size() == 0
    }

    void "delete project removes all associated environments"() {
        given:
        def ed = DataFactory.environment()
        Environment env = new Environment(ed)
        def pd = DataFactory.project()
        Project project = new Project(name: pd.name, code: pd.code, environments: [env]).save()

        expect:
        project.id != null
        env.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "area and project are deleted"
        projectService.get(project.id) == null
        environmentService.get(env.id) == null
    }

    void "saveUpdate removes environments"() {
        given: "project with environment"
        def env = new Environment(name: "env name").save()
        Project project = new Project(name: "Remove Area Test", code: "RAT").addToEnvironments(env).save()

        expect:
        project.environments.size() == 1
        env.id != null

        when: "call saveUpdate"
        def items = new RemovedItems()
        items.environmentIds = [env.id]
        projectService.saveUpdate(project, items)
        sessionFactory.currentSession.flush()

        then: "env is removed"
        project.environments.size() == 0
    }
}
