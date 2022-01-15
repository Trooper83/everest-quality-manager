package com.everlution.test.releaseplan

import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCycle
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class ReleasePlanHibernateSpec extends HibernateSpec {

    @Shared Project project

    def setup() {
        project = new Project(name: "Test Case Date Project", code: "TCD").save()
    }

    void "test date created auto generated"() {
        when:
        def plan = new ReleasePlan(name: "name", project: project).save()

        then:
        plan.dateCreated != null
    }

    void "save does not cascade to project"() {
        when: "unsaved project is added to plan"
        def p = new Project(name: "fail", code: "fai")
        new ReleasePlan(name: "name", project: p).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete plan does not cascade to project"() {
        given:
        def plan = new ReleasePlan(name: "name", project: project).save()

        when: "delete test case"
        plan.delete()
        sessionFactory.currentSession.flush()

        then: "project is not deleted"
        ReleasePlan.findById(plan.id) == null
        Project.findById(project.id) != null
    }

    void "delete release plan cascades to test cycles"() {
        given:
        def cycle = new TestCycle(name: "cycle name")
        def plan = new ReleasePlan(name: "name", project: project).addToTestCycles(cycle).save()

        expect:
        ReleasePlan.findById(plan.id) != null
        TestCycle.findById(cycle.id) != null

        when:
        plan.delete(flush: true)

        then:
        ReleasePlan.findById(plan.id) == null
        TestCycle.findById(cycle.id) == null
    }

    void "save release plan cascades to test cycles"() {
        when:
        def cycle = new TestCycle(name: "cycle name")
        def plan = new ReleasePlan(name: "name", project: project).addToTestCycles(cycle).save()

        then:
        ReleasePlan.findById(plan.id) != null
        TestCycle.findById(cycle.id) != null
    }

    void "removeFromTestCycles deletes test cycles"() {
        given:
        def cycle = new TestCycle(name: "cycle name")
        def plan = new ReleasePlan(name: "name", project: project).addToTestCycles(cycle).save()

        expect:
        ReleasePlan.findById(plan.id) != null
        TestCycle.findById(cycle.id) != null

        when:
        plan.removeFromTestCycles(cycle).save(flush: true)

        then:
        TestCycle.findById(cycle.id) == null
    }
}
