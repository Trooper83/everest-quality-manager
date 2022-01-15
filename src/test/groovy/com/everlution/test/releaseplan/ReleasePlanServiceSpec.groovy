package com.everlution.test.releaseplan

import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class ReleasePlanServiceSpec extends Specification implements ServiceUnitTest<ReleasePlanService>, DataTest {

    def setupSpec() {
        mockDomain(ReleasePlan)
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
}
