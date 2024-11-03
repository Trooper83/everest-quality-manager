package com.manager.quality.everest.test.unit.project

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.domains.Scenario
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestGroup
import grails.test.hibernate.HibernateSpec
import spock.lang.Shared

import javax.persistence.PersistenceException

class ProjectHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Project, Bug, ReleasePlan, Scenario, TestCase] }

    @Shared Person person

    def setup() {
        person = new Person(email: "test@test.com", password: "!Password2022").save()
    }

    void "delete project with bug throws persistence exception"() {
        given: "valid bug with a project"
        Project project = new Project(name: "Delete Bug Cascade Project777", code: "ZZ7").save()
        new Bug(name: "cascade project", description: "this should delete", person: person,
                project: project, status: "Open", actual: "actual", expected: "expected").save()

        when: "delete project"
        project.delete()
        sessionFactory.currentSession.flush()

        then: "exception is thrown"
        thrown(PersistenceException)
    }

    void "delete project with test case throws persistence exception"() {
        given: "valid test case with project"
        Project project = new Project(name: "Delete Test Case Cascade Project000", code: "ZZ0").save()
        new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        when: "delete project"
        project.delete()
        sessionFactory.currentSession.flush()

        then: "exception is thrown"
        thrown(PersistenceException)
    }

    void "delete project with scenario throws persistence exception"() {
        given: "valid scenario with project"
        Project project = new Project(name: "Delete Test Case Cascade Project001", code: "Z16").save()
        new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        when: "delete project"
        project.delete()
        sessionFactory.currentSession.flush()

        then: "exception is thrown"
        thrown(PersistenceException)
    }

    void "delete project with release plan throws persistence exception"() {
        given: "valid scenario with project"
        Project project = new Project(name: "Delete Test Case Cascade Project001", code: "ZZ6").save()
        new ReleasePlan(name: "test plan", project: project, status: "ToDo", person: person).save()

        when: "delete project"
        project.delete()
        sessionFactory.currentSession.flush()

        then: "exception is thrown"
        thrown(PersistenceException)
    }

    void "delete project cascades to area"() {
        given: "project with valid area params"
        Project p = new Project(name: "Cascades To Area", code: "CTA", areas: [new Area(name: "area name")]).save()

        expect: "area is saved"
        Area.count() == 1

        when: "delete project"
        p.delete()
        sessionFactory.currentSession.flush()

        then: "area was deleted"
        Area.count() == 0
    }

    void "removeFrom project deletes orphaned area"() {
        given: "project with valid area params"
        def a = new Area(name: "area name")
        Project p = new Project(name: "Cascades To Area", code: "CTA", areas: [a]).save()

        expect: "area is saved"
        Area.findById(a.id) != null

        when: "remove area from project"
        p.removeFromAreas(a).save(flush: true)

        then: "area was deleted"
        Area.findById(a.id) == null
    }

    void "update project cascades to area"() {
        given: "project with valid area params"
        Project p = new Project(name: "Cascades To Area", code: "CTA", areas: [new Area(name: "area name")]).save()

        expect: "area is saved"
        p.areas[0].name == "area name"

        when: "update area"
        p.areas[0].name = "edited name"
        p.save()
        sessionFactory.currentSession.flush()

        then: "area was updated"
        Project.findById(p.id).areas[0].name == "edited name"
    }

    void "removeFrom project deletes orphaned environments"() {
        given: "project with valid area params"
        def e = new Environment(name: "env name")
        Project p = new Project(name: "Cascades To Area", code: "CTA", environments: [e]).save()

        expect: "env is saved"
        Environment.findById(e.id) != null

        when: "remove env from project"
        p.removeFromEnvironments(e).save(flush: true)

        then: "env was deleted"
        Environment.findById(e.id) == null
    }

    void "delete project cascades to environment"() {
        given: "project with valid area params"
        Project p = new Project(name: "Cascades To Env", code: "CTE", environments: [new Environment(name: "env name")]).save()

        expect: "env is saved"
        Environment.count() == 1

        when: "delete project"
        p.delete()
        sessionFactory.currentSession.flush()

        then: "env was deleted"
        Environment.count() == 0
    }

    void "update project cascades to environment"() {
        given: "project with valid env params"
        Project p = new Project(name: "Cascades To Env", code: "CTE", environments: [new Environment(name: "env name")]).save()

        expect: "env is saved"
        p.environments[0].name == "env name"

        when: "update env"
        p.environments[0].name = "edited name"
        p.save()
        sessionFactory.currentSession.flush()

        then: "env was updated"
        Project.findById(p.id).environments[0].name == "edited name"
    }

    void "delete project cascades to test group"() {
        given: "project with valid params"
        Project p = new Project(name: "Cascades To Group", code: "CTE").save()
        def group = new TestGroup(name: "group name", project: p).save()

        expect: "group is saved"
        group.id != null

        when: "delete project"
        p.delete()
        sessionFactory.currentSession.flush()

        then: "exception thrown"
        thrown(PersistenceException)
    }

    void "removeFromPlatforms deletes orphaned platform"() {
        given: "project with valid platform params"
        def pl = new Platform(name: "platform name")
        Project p = new Project(name: "Cascades To Platform", code: "CTA", platforms: [pl]).save()

        expect: "env is saved"
        Platform.findById(pl.id) != null

        when: "remove from project"
        p.removeFromPlatforms(pl).save(flush: true)

        then: "it was deleted"
        Platform.findById(pl.id) == null
    }

    void "delete project cascades to platform"() {
        given:
        Project p = new Project(name: "Cascades To Platform", code: "CTP", platforms: [new Platform(name: "platform name")]).save()

        expect:
        Platform.count() == 1

        when: "delete project"
        p.delete()
        sessionFactory.currentSession.flush()

        then: "it was deleted"
        Platform.count() == 0
    }

    void "update project cascades to platform"() {
        given: "project with valid platform params"
        Project p = new Project(name: "Cascades To Env", code: "CTE", platforms: [new Platform(name: "platform name")]).save()

        expect: "platform is saved"
        p.platforms[0].name == "platform name"

        when: "update env"
        p.platforms[0].name = "edited name"
        p.save()
        sessionFactory.currentSession.flush()

        then: "env was updated"
        Project.findById(p.id).platforms[0].name == "edited name"
    }
}
