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
        new ReleasePlan(name: "first plan", project: project, status: "ToDo").save()
        new ReleasePlan(name: "second plan", project: project, status: "ToDo").save()
    }

    void "get with valid id returns instance"() {
        when:
        def pr = new Project(name: "name", code: "cod").save()
        new ReleasePlan(name: "name", project: pr, status: "ToDo").save()

        then:
        service.get(1) instanceof ReleasePlan
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(99999) == null
    }

    void "read with invalid id returns null"() {
        expect:
        service.read(999999) == null
    }

    void "read returns instance"() {
        when:
        def pr = new Project(name: "name", code: "cod").save()
        new ReleasePlan(name: "name", project: pr, status: "ToDo").save()

        then:
        service.read(1) instanceof ReleasePlan
    }

    void "delete with valid id deletes instance"() {
        given:
        def pr = new Project(name: "name", code: "cod").save()
        new ReleasePlan(name: "name", project: pr, status: "ToDo").save()
        new ReleasePlan(name: "name", project: pr, status: "ToDo").save()
        def id = new ReleasePlan(name: "name", project: pr, status: "ToDo").save(flush: true).id

        expect:
        service.get(id) != null

        when:
        service.delete(id)

        then:
        service.get(id) == null
    }

    void "delete with invalid id does not throw exception"() {
        when:
        service.delete(null)

        then:
        noExceptionThrown()
    }

    void "save with valid plan returns instance"() {
        given:
        def pr = new Project(name: "name", code: "cod").save()
        def plan = new ReleasePlan(name: "name", project: pr, status: "ToDo")

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
        ReleasePlan plan = new ReleasePlan(name: "name", project: project, status: "ToDo").addToTestCycles(cycle).save()

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
        def plans = service.findAllByProject(project, [:])

        then:
        plans.results instanceof List<ReleasePlan>
    }

    void "find all by project only returns plans with project"() {
        given:
        def proj = new Project(name: "Plan Project1223", code: "BP8").save()
        def plan = new ReleasePlan(name: "Name of the plan", project: proj, status: "ToDo").save(flush: true)
        new ReleasePlan(name: "Name of the plan123", project: project, status: "ToDo").save(flush: true)

        expect:
        ReleasePlan.list().contains(plan)

        when:
        def plans = service.findAllByProject(project, [:]).results

        then:
        plans.every { it.project.id == project.id }
        plans.size() > 0
        !plans.contains(plan)
    }

    void "find all by project with null project id returns empty list"() {
        when:
        def plans = service.findAllByProject(null, [:])

        then:
        noExceptionThrown()
        plans.results.size() == 0
        plans.count == 0
    }

    void "add test cycle saves test cycle"() {
        given:
        def plan = new ReleasePlan(name: "Name of the plan123", project: project, status: "ToDo").save(flush: true)
        def cycle = new TestCycle()

        expect:
        cycle.id == null

        when:
        service.addTestCycle(plan, cycle)

        then:
        cycle.id != null
    }

    void "find all by name ilike returns plans"(String q) {
        expect:
        def plans = service.findAllInProjectByName(project, q, [:])
        plans.results.first().name == "first plan"

        where:
        q << ['first', 'fi', 'irs', 't pl', 'FIRST']
    }

    void "find all in project by name only returns plans in project"() {
        given:
        def proj = new Project(name: "TestService Spec Project99999", code: "BP5").save()
        def plan = new ReleasePlan(name: "Name of the plan", project: proj, status: "ToDo").save(flush: true)

        expect:
        ReleasePlan.list().contains(plan)

        when:
        def plans = service.findAllInProjectByName(project, 'plan', [:]).results

        then:
        plans.every { it.project.id == project.id }
        plans.size() > 0
        !plans.contains(plan)
    }

    void "find all in project by name with null project"() {
        when:
        def plans = service.findAllInProjectByName(null, 'plan', [:])

        then:
        plans.results.empty
        plans.count == 0
        noExceptionThrown()
    }

    void "find all by name ilike with string"(String s, int size) {
        expect:
        def plans = service.findAllInProjectByName(project, s, [:])
        plans.results.size() == size
        plans.count == size

        where:
        s           | size
        null        | 0
        ''          | 2
        'not found' | 0
        'plan'      | 2
    }
}
