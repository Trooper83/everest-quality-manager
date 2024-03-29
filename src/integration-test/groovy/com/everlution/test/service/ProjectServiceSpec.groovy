package com.everlution.test.service

import com.everlution.domains.Area
import com.everlution.services.area.AreaService
import com.everlution.domains.Bug
import com.everlution.domains.Environment
import com.everlution.services.environment.EnvironmentService
import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.domains.ReleasePlan
import com.everlution.domains.Scenario
import com.everlution.domains.TestCase
import com.everlution.controllers.command.RemovedItems
import com.everlution.test.support.DataFactory
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

import javax.persistence.PersistenceException

@Integration
@Rollback
class ProjectServiceSpec extends Specification {

    AreaService areaService
    EnvironmentService environmentService
    PersonService personService
    ProjectService projectService
    SessionFactory sessionFactory

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

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

    void "get returns null for not found id"() {
        setupData()

        expect:
        projectService.get(9999999999) == null
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

    void "save throws exception with validation fail"() {
        when:
        Project project = new Project(name: "testing save project")
        projectService.save(project)

        then:
        thrown(ValidationException)
    }

    void "save throws exception with two areas with same name"() {
        when:
        def area = new Area(name: "area")
        def area1 = new Area(name: "area")
        Project project = new Project(name: "testing save project", areas: [area, area1])
        projectService.save(project)

        then:
        thrown(ValidationException)
    }

    void "save throws exception with two environments with same name"() {
        when:
        def env = new Environment(name: "env")
        def env1 = new Environment(name: "env")
        Project project = new Project(name: "testing save project", areas: [env, env1])
        projectService.save(project)

        then:
        thrown(ValidationException)
    }

    void "delete throws constraint exception when project has associated scenarios"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZD").save()
        def scenario = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        expect:
        project.id != null
        scenario.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "exception thrown"
        thrown(PersistenceException)
    }

    void "delete throws constraint exception when project has associated test case"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
        TestCase testCase = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        expect:
        project.id != null
        testCase.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "exception thrown"
        thrown(PersistenceException)
    }

    void "delete throws constraint exception when project has associated bug"() {
        given:
        Project project = new Project(name: "Test Case Service Spec Project", code: "ZZC").save()
        Bug bug = new Bug(name: "cascade project", description: "this should delete", person: person,
                project: project, status: "Open", actual: "actual", expected: "expected").save()

        expect:
        project.id != null
        bug.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "exception thrown"
        thrown(PersistenceException)
    }

    void "delete throws constraint exception when project has associated release plans"() {
        given:
        Project project = new Project(name: "Release Plan Service Spec Project", code: "ZZX").save()
        def plan = new ReleasePlan(name: "name", project: project, status: "ToDo", person: person).save()

        expect:
        project.id != null
        plan.id != null

        when: "delete project"
        projectService.delete(project.id)
        sessionFactory.currentSession.flush()

        then: "exception thrown"
        thrown(PersistenceException)
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

    void "saveUpdate throws exception with two areas with same name"() {
        when:
        def area = new Area(name: "area")
        def area1 = new Area(name: "area")
        Project project = new Project(name: "testing save project", code: 'ttt', areas: [area, area1])
        projectService.saveUpdate(project, new RemovedItems())

        then:
        thrown(ValidationException)
    }

    void "saveUpdate throws exception with two environments with same name"() {
        when:
        def env = new Environment(name: "env")
        def env1 = new Environment(name: "env")
        Project project = new Project(name: "testing save project", code: "ccx", environments: [env, env1])
        projectService.saveUpdate(project, new RemovedItems())

        then:
        thrown(ValidationException)
    }

    void "saveUpdate throws exception with no code validation fail"() {
        when:
        Project project = new Project(name: "testing save project")
        projectService.saveUpdate(project, new RemovedItems())

        then:
        thrown(ValidationException)
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

    void "read returns instance"() {
        setup:
        def id = setupData()

        expect:
        projectService.read(id) instanceof Project
    }

    void "read returns null for not found id"() {
        expect:
        projectService.read(999999999) == null
    }

    void "find all by name ilike returns project"() {
        setup:
        setupData()

        expect:
        def project = projectService.findAllByNameIlike("service 1", [:])
        project.results.first().name == "Project service 1"
    }
}
