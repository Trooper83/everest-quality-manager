package com.everlution.test.service

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.TestCycle
import com.everlution.TestCycleService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ReleasePlanServiceSpec extends Specification {

    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCycleService testCycleService
    SessionFactory sessionFactory

    @Shared Project project

    private Long setupData() {
        project = new Project(name: "project name 123", code: "pn1").save()
        def releasePlan = new ReleasePlan(name: "name1", project: project, status: "ToDo").save()
        new ReleasePlan(name: "name12", project: project, status: "ToDo").save()
        new ReleasePlan(name: "name123", project: project, status: "ToDo").save()
        new ReleasePlan(name: "name124", project: project, status: "ToDo").save(flush: true, failOnError: true)
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
        def project = projectService.list(max: 1).first()
        ReleasePlan releasePlan = new ReleasePlan(name: "test name", project: project, status: "ToDo")
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
        ReleasePlan releasePlan = new ReleasePlan(name: "test name", project: project, testCycles: [cycle], status: "ToDo")
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
        def releasePlan = new ReleasePlan(name: "test name123", project: project, status: "ToDo")
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
        def releasePlan = new ReleasePlan(name: "test name123", project: project, status: "ToDo")
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
}
