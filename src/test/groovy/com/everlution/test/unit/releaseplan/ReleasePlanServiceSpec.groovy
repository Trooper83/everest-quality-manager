package com.everlution.test.unit.releaseplan

import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.ReleasePlan
import com.everlution.services.releaseplan.ReleasePlanService
import com.everlution.domains.TestCycle
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

import java.time.Instant
import java.time.temporal.ChronoUnit

class ReleasePlanServiceSpec extends Specification implements ServiceUnitTest<ReleasePlanService>, DataTest {

    @Shared Person person
    @Shared Project project

    def setupSpec() {
        mockDomain(ReleasePlan)
    }

    def setup() {
        project = new Project(name: "name", code: "cod").save()
        person = new Person(email: "testemail@email.com", password: "NewPassword#2023!").save()
        new ReleasePlan(name: "first plan", project: project, status: "ToDo", person: person).save()
        new ReleasePlan(name: "second plan", project: project, status: "ToDo", person: person).save()
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
        new ReleasePlan(name: "name", project: pr, status: "ToDo", person: person).save()
        new ReleasePlan(name: "name", project: pr, status: "ToDo", person: person).save()
        def id = new ReleasePlan(name: "name", project: pr, status: "ToDo", person: person).save(flush: true).id

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
        def plan = new ReleasePlan(name: "name", project: pr, status: "ToDo", person: person)

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
        ReleasePlan plan = new ReleasePlan(name: "name", project: project, status: "ToDo", person: person)
                .addToTestCycles(cycle).save()

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
        def plan = new ReleasePlan(name: "Name of the plan", project: proj, status: "ToDo", person: person).save(flush: true)
        new ReleasePlan(name: "Name of the plan123", project: project, status: "ToDo", person: person).save(flush: true)

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
        def plan = new ReleasePlan(name: "Name of the plan123", project: project, status: "ToDo", person: person)
                .save(flush: true)
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
        def plan = new ReleasePlan(name: "Name of the plan", project: proj, status: "ToDo", person: person).save(flush: true)

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

    void "getReleaseByStatus returns next releases"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date1 = new Date().from(Instant.now().plus(10, ChronoUnit.DAYS))
        def date2 = new Date().from(Instant.now().plus(11, ChronoUnit.DAYS))
        def date3 = new Date().from(Instant.now().plus(12, ChronoUnit.DAYS))
        def date4 = new Date().from(Instant.now().plus(13, ChronoUnit.DAYS))
        def p1 = new ReleasePlan(name: "first plan", project: p, status: "Planning", person: person, plannedDate: date1).save()
        def p2 = new ReleasePlan(name: "first plan", project: p, status: "Planning", person: person, plannedDate: date2).save()
        def p3 = new ReleasePlan(name: "first plan", project: p, status: "Planning", person: person, plannedDate: date3).save()
        new ReleasePlan(name: "first plan", project: p, status: "Planning", person: person, plannedDate: date4).save()
        new ReleasePlan(name: "second plan", project: p, status: "ToDo", person: person, plannedDate: date4).save(flush: true)

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.next.size() == 3
        plans.next.first() == p1
        plans.next[1] == p2
        plans.next[2] == p3
    }

    void "getPlansByStatus returns previous releases"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date1 = new Date().from(Instant.now().minus(10, ChronoUnit.DAYS))
        def date2 = new Date().from(Instant.now().minus(11, ChronoUnit.DAYS))
        def date3 = new Date().from(Instant.now().minus(12, ChronoUnit.DAYS))
        def date4 = new Date().from(Instant.now().minus(13, ChronoUnit.DAYS))
        def p1 = new ReleasePlan(name: "first plan", project: p, status: "Released", person: person, releaseDate: date1).save()
        def p2 = new ReleasePlan(name: "first plan", project: p, status: "Released", person: person, releaseDate: date2).save()
        def p3 = new ReleasePlan(name: "first plan", project: p, status: "Released", person: person, releaseDate: date3).save()
        new ReleasePlan(name: "second plan", project: p, status: "Released", person: person, releaseDate: date4).save(flush: true)

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.released.size() == 3
        plans.released.first() == p1
        plans.released[1] == p2
        plans.released[2] == p3
    }

    void "getPlansByStatus returns in progress releases"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date1 = new Date().from(Instant.now().plus(10, ChronoUnit.DAYS))
        def date2 = new Date().from(Instant.now().plus(11, ChronoUnit.DAYS))
        def date3 = new Date().from(Instant.now().plus(12, ChronoUnit.DAYS))
        def date4 = new Date().from(Instant.now().plus(13, ChronoUnit.DAYS))
        def p1 = new ReleasePlan(name: "first plan", project: p, status: "In Progress", person: person, plannedDate: date1).save()
        def p2 = new ReleasePlan(name: "first plan", project: p, status: "In Progress", person: person, plannedDate: date2).save()
        def p3 = new ReleasePlan(name: "first plan", project: p, status: "In Progress", person: person, plannedDate: date3).save()
        new ReleasePlan(name: "second plan", project: p, status: "In Progress", person: person, plannedDate: date4).save(flush: true)

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.inProgress.size() == 3
        plans.inProgress.first() == p1
        plans.inProgress[1] == p2
        plans.inProgress[2] == p3
    }

    void "getPlansByStatus returns empty list when no plans found"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()

        expect:
        p != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.inProgress.empty
        plans.released.empty
        plans.next.empty
    }

    void "getPlansByStatus returns next release even if in past"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date = new Date().from(Instant.now().minus(10, ChronoUnit.DAYS))
        def plan = new ReleasePlan(name: "second plan", project: p, status: "Planning", person: person, plannedDate: date).save(flush: true)

        expect:
        plan != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.next.first() == plan
    }

    void "getPlansByStatus returns previous release even if in future"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date = new Date().from(Instant.now().plus(10, ChronoUnit.DAYS))
        def plan = new ReleasePlan(name: "second plan", project: p, status: "Released", person: person, releaseDate: date).save(flush: true)

        expect:
        plan != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.released.first() == plan
    }

    void "getPlansByStatus does not return canceled plans"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date = new Date().from(Instant.now().minus(10, ChronoUnit.DAYS))
        def plan = new ReleasePlan(name: "second plan", project: p, status: "Canceled", person: person, plannedDate: date).save(flush: true)

        expect:
        plan != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.next.empty
    }

    void "getPlansByStatus does not return todo plans"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date = new Date().from(Instant.now().minus(10, ChronoUnit.DAYS))
        def plan = new ReleasePlan(name: "second plan", project: p, status: "ToDo", person: person, plannedDate: date).save(flush: true)

        expect:
        plan != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.next.empty
    }

    void "getPlansByStatus does not return released plans with no releaseDate"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def plan = new ReleasePlan(name: "second plan", project: p, status: "Released", person: person, releaseDate: null).save(flush: true)

        expect:
        plan.id != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.released.empty
    }

    void "getPlansByStatus does not return in progress plans with no plannedDate"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def plan = new ReleasePlan(name: "second plan", project: p, status: "In Progress", person: person, plannedDate: null).save(flush: true)

        expect:
        plan.id != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.inProgress.empty
    }

    void "getPlansByStatus does not return in progress plans with releaseDate"() {
        given:
        def p = new Project(name: "name123", code: "cod22").save()
        def date = new Date().from(Instant.now().minus(10, ChronoUnit.DAYS))
        def plan = new ReleasePlan(name: "second plan", project: p, status: "In Progress", person: person,
                plannedDate: date, releaseDate: date).save(flush: true)

        expect:
        plan.id != null

        when:
        def plans = service.getPlansByStatus(p)

        then:
        plans.inProgress.empty
    }
}
