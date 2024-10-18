package com.manager.quality.everest.test.service

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.services.testcycle.TestCycleService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

import java.time.Instant
import java.time.temporal.ChronoUnit

@Integration
@Rollback
class ReleasePlanServiceSpec extends Specification {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCycleService testCycleService
    SessionFactory sessionFactory

    @Shared Person person
    @Shared Project project

    private Long setupData() {
        person = new Person(email: "testing9876@testing.com", password: "#Password#2023!").save()
        project = new Project(name: "project name 123", code: "pn1").save()
        def releasePlan = new ReleasePlan(name: "name1", project: project, status: "ToDo", person: person).save()
        new ReleasePlan(name: "name12", project: project, status: "ToDo", person: person).save()
        new ReleasePlan(name: "name123", project: project, status: "ToDo", person: person).save()
        new ReleasePlan(name: "name124", project: project, status: "ToDo", person: person).save(flush: true, failOnError: true)
        releasePlan.id
    }

    void "get returns instance"() {
        setupData()

        expect:
        releasePlanService.get(1) instanceof ReleasePlan
    }

    void "read returns instance"() {
        setupData()

        expect:
        releasePlanService.read(1) instanceof ReleasePlan
    }

    void "delete removes plan"() {
        Long releasePlanId = setupData()

        expect:
        ReleasePlan.findById(releasePlanId) != null

        when:
        releasePlanService.delete(releasePlanId)
        sessionFactory.currentSession.flush()

        then:
        ReleasePlan.findById(releasePlanId) == null
    }

    void "save persists instance"() {
        when:
        def proj = projectService.list(max: 1).first()
        def per = personService.list(max: 1).first()
        ReleasePlan releasePlan = new ReleasePlan(name: "test name", project: proj, status: "ToDo", person: per)
        releasePlanService.save(releasePlan)

        then:
        releasePlan.id != null
    }

    void "save throws validation exception for invalid instance"() {
        when:
        ReleasePlan releasePlan = new ReleasePlan(name: "test name")
        releasePlanService.save(releasePlan)

        then:
        thrown(ValidationException)
    }

    void "removeTestCycle removes and deletes test cycle"() {
        given:
        def cycle = new TestCycle(name: "test cycle")
        def project = projectService.list(max: 1).first()
        def per = personService.list(max: 1).first()
        ReleasePlan releasePlan = new ReleasePlan(name: "test name", project: project, testCycles: [cycle],
                status: "ToDo", person: per)
        releasePlanService.save(releasePlan)

        expect:
        testCycleService.get(cycle.id) != null

        when:
        releasePlanService.removeTestCycle(releasePlan, cycle)
        sessionFactory.currentSession.flush()

        then:
        testCycleService.get(cycle.id) == null
    }

    void "find all by project returns only plans with project"() {
        given:
        setupData()

        when:
        def project = projectService.list(max: 1).first()
        def plans = releasePlanService.findAllByProject(project, [:]).results

        then:
        plans.size() > 0
        plans.every { it.project.id == project.id }
    }

    void "find all by project with null project returns empty list"() {
        given:
        setupData()

        when:
        def plans = releasePlanService.findAllByProject(null, [:])

        then:
        plans.results.size() == 0
        plans.count == 0
        noExceptionThrown()
    }

    void "add test cycle persists test cycle"() {
        given:
        def cycle = new TestCycle(name: "test cycle123")
        def project = projectService.list(max: 1).first()
        def per = personService.list(max: 1).first()
        def releasePlan = new ReleasePlan(name: "test name123", project: project, status: "ToDo", person: per)
        releasePlanService.save(releasePlan)

        expect:
        cycle.id == null
        !releasePlan.testCycles

        when:
        releasePlanService.addTestCycle(releasePlan, cycle)
        sessionFactory.currentSession.flush()

        then:
        cycle.id != null
        releasePlan.testCycles.size() == 1
    }

    void "add test cycle with invalid test cycle throws error"() {
        given:
        def cycle = new TestCycle()
        def project = projectService.list(max: 1).first()
        def per = personService.list(max: 1).first()
        def releasePlan = new ReleasePlan(name: "test name123", project: project, status: "ToDo", person: per)
        releasePlanService.save(releasePlan)

        when:
        releasePlanService.addTestCycle(releasePlan, cycle)
        sessionFactory.currentSession.flush()

        then:
        thrown(ValidationException)
    }

    void "find all in project by name returns plans"() {
        setup:
        setupData()

        expect:
        def plan = releasePlanService.findAllInProjectByName(project, "124", [:])
        plan.results.first().name == "name124"
    }

    void "getPlansByStatus returns releases"() {
        given:
        def proj = projectService.list(max: 1).first()
        def per = personService.list(max: 1).first()
        def pDate = new Date().from(Instant.now().minus(10, ChronoUnit.DAYS))
        ReleasePlan prev = new ReleasePlan(name: "test name", project: proj, status: "Released", person: per, releaseDate: pDate)
        ReleasePlan inProgress = new ReleasePlan(name: "test name", project: proj, status: "In Progress", person: per, plannedDate: pDate)
        releasePlanService.save(prev)
        releasePlanService.save(inProgress)

        when:
        def plans = releasePlanService.getPlansByStatus(proj)

        then:
        plans.released.first() == prev
        plans.inProgress.first() == inProgress
    }
}
