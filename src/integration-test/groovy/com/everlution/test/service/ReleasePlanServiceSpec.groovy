package com.everlution.test.service

import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.TestCycle
import com.everlution.TestCycleService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class ReleasePlanServiceSpec extends Specification {

    ReleasePlanService releasePlanService
    TestCycleService testCycleService
    SessionFactory sessionFactory

    private Long setupData() {
        def project = new Project(name: "project name 123", code: "pn1").save()
        def releasePlan = new ReleasePlan(name: "name1", project: project).save()
        new ReleasePlan(name: "name12", project: project).save()
        new ReleasePlan(name: "name123", project: project).save()
        new ReleasePlan(name: "name124", project: project).save(flush: true, failOnError: true)
        releasePlan.id
    }

    void "get returns instance"() {
        setupData()

        expect:
        releasePlanService.get(1) instanceof ReleasePlan
    }

    void "list with args"() {
        setupData()

        when:
        List<ReleasePlan> releasePlanList = releasePlanService.list(max: 2, offset: 2)

        then:
        releasePlanList.size() == 2
    }

    void "count returns number of plans"() {
        setupData()

        expect:
        releasePlanService.count() == 5
    }

    void "delete removes plan"() {
        Long releasePlanId = setupData()

        expect:
        releasePlanService.count() == 5

        when:
        releasePlanService.delete(releasePlanId)
        sessionFactory.currentSession.flush()

        then:
        releasePlanService.count() == 4
    }

    void "save persists instance"() {
        when:
        def project = new Project(name: "project name 123", code: "pn1").save()
        ReleasePlan releasePlan = new ReleasePlan(name: "test name", project: project)
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
        def project = new Project(name: "project name 123", code: "pn1").save()
        def cycle = new TestCycle(name: "test cycle")
        ReleasePlan releasePlan = new ReleasePlan(name: "test name", project: project, testCycles: [cycle])
        releasePlanService.save(releasePlan)

        expect:
        testCycleService.get(cycle.id) != null

        when:
        releasePlanService.removeTestCycle(releasePlan, cycle)
        sessionFactory.currentSession.flush()

        then:
        testCycleService.get(cycle.id) == null
    }
}
