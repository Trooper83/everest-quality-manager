package com.everlution.test.releaseplan

import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.TestCycle
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class ReleasePlanServiceSpec extends Specification implements ServiceUnitTest<ReleasePlanService>, DataTest {

    @Shared Project project

    def setupSpec() {
        mockDomain(ReleasePlan)
    }

    def setup() {
        project = new Project(name: "name", code: "cod").save()
    }

    void "get with valid id returns instance"() {
        when:
        def pr = new Project(name: "name", code: "cod").save()
        new ReleasePlan(name: "name", project: pr).save()

        then:
        service.get(1) instanceof ReleasePlan
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(1) == null
    }

    void "list max args param returns correct value"() {
        when:
        def pr = new Project(name: "name", code: "cod").save()
        new ReleasePlan(name: "name", project: pr).save()
        new ReleasePlan(name: "name", project: pr).save()
        new ReleasePlan(name: "name", project: pr).save(flush: true)

        then:
        service.list(max: 1).size() == 1
        service.list(max: 2).size() == 2
        service.list().size() == 3
        service.list(offset: 1).size() == 2
    }

    void "count returns number of plans"() {
        when:
        def pr = new Project(name: "name", code: "cod").save()
        new ReleasePlan(name: "name", project: pr).save()
        new ReleasePlan(name: "name", project: pr).save()
        new ReleasePlan(name: "name", project: pr).save(flush: true)

        then:
        service.count() == 3
    }

    void "delete with valid id deletes instance"() {
        given:
        def pr = new Project(name: "name", code: "cod").save()
        new ReleasePlan(name: "name", project: pr).save()
        new ReleasePlan(name: "name", project: pr).save()
        def id = new ReleasePlan(name: "name", project: pr).save(flush: true).id

        expect:
        service.get(id) != null

        when:
        service.delete(id)

        then:
        service.get(id) == null
    }

    void "save with valid plan returns instance"() {
        given:
        def pr = new Project(name: "name", code: "cod").save()
        def plan = new ReleasePlan(name: "name", project: pr)

        when:
        def saved = service.save(plan)

        then:
        saved instanceof ReleasePlan
    }

    void "save with invalid plan throws validation exception"() {
        given:
        def plan = new ReleasePlan(name: "name")

        when:
        service.save(plan)

        then:
        thrown(ValidationException)
    }

    void "removeTestCycle removes test cycle"() {
        given:
        def cycle = new TestCycle(name: "testing cycle")
        ReleasePlan plan = new ReleasePlan(name: "name", project: project).addToTestCycles(cycle).save()

        expect:
        cycle.releasePlan == plan
        plan.testCycles.size() == 1

        when:
        service.removeTestCycle(plan, cycle)

        then:
        cycle.releasePlan == null
        plan.testCycles.empty
    }

    void "find all by project returns plans"() {
        when:
        def plans = service.findAllByProject(project)

        then:
        plans instanceof List<ReleasePlan>
    }

    void "find all by project only returns plans with project"() {
        given:
        def proj = new Project(name: "Plan Project1223", code: "BP8").save()
        def plan = new ReleasePlan(name: "Name of the plan", project: proj).save(flush: true)
        new ReleasePlan(name: "Name of the plan123", project: project).save(flush: true)

        expect:
        ReleasePlan.list().contains(plan)

        when:
        def plans = service.findAllByProject(project)

        then:
        plans.every { it.project.id == project.id }
        plans.size() > 0
        !plans.contains(plan)
    }

    void "find all by project with null project id returns empty list"() {
        when:
        def plans = service.findAllByProject(null)

        then:
        noExceptionThrown()
        plans.size() == 0
    }

    void "add test cycle saves test cycle"() {
        given:
        def plan = new ReleasePlan(name: "Name of the plan123", project: project).save(flush: true)
        def cycle = new TestCycle()

        expect:
        cycle.id == null

        when:
        service.addTestCycle(plan, cycle)

        then:
        cycle.id != null
    }
}
