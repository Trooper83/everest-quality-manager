package com.everlution.test.testcycle

import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCycle
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class TestCycleHibernateSpec extends HibernateSpec {

    @Shared Project project

    def setup() {
        project = new Project(name: "Test Case Date Project For Cycle", code: "TCC").save()
    }

    void "test date created auto generated"() {
        when:
        def cycle = new TestCycle(name: "testing")
        new ReleasePlan(name: "name", project: project).addToTestCycles(cycle).save()

        then:
        cycle.dateCreated != null
    }

    void "save cycle does not cascade to release plan"() {
        given: "unsaved release plan"
        def plan = new ReleasePlan(name: "this is the name", project: project)

        when: "save test cycle"
        new TestCycle(name: "test cycle", releasePlan: plan).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete cycle does not cascade to release plan"() {
        given: "saved release plan & cycle"
        def plan = new ReleasePlan(name: "this is the name", project: project).save()
        TestCycle cycle = new TestCycle(name: "test cycle", releasePlan: plan).save()

        expect:
        TestCycle.findById(cycle.id) != null
        ReleasePlan.findById(plan.id) != null

        when: "delete test cycle"
        cycle.delete(flush: true)

        then: "cycle deleted & plan is not"
        TestCycle.findById(cycle.id) == null
        ReleasePlan.findById(plan.id) != null
    }
}
