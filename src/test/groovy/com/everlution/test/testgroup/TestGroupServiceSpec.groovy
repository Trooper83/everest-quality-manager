package com.everlution.test.testgroup

import com.everlution.TestGroup
import com.everlution.TestGroupService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class TestGroupServiceSpec extends Specification implements ServiceUnitTest<TestGroupService>, DataTest {

    def setupSpec() {
        mockDomain(TestGroup)
    }

    void "get with valid id returns instance"() {
        when:
        new TestGroup(name: "name").save()

        then:
        service.get(1) instanceof TestGroup
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(1) == null
    }

    void "list max args param returns correct value"() {
        when:
        new TestGroup(name: "name").save()
        new TestGroup(name: "name1").save()
        new TestGroup(name: "name2").save(flush: true)

        then:
        service.list(max: 1).size() == 1
        service.list(max: 2).size() == 2
        service.list().size() == 3
        service.list(offset: 1).size() == 2
    }

    void "count returns number of groups"() {
        when:
        new TestGroup(name: "name").save()
        new TestGroup(name: "name1").save()
        new TestGroup(name: "name2").save(flush: true)

        then:
        service.count() == 3
    }

    void "delete with valid id deletes instance"() {
        given:
        new TestGroup(name: "name").save()
        new TestGroup(name: "name1").save()
        def id = new TestGroup(name: "name2").save(flush: true).id

        expect:
        service.get(id) != null

        when:
        service.delete(id)

        then:
        service.get(id) == null
    }

    void "save with valid group returns instance"() {
        given:
        def group = new TestGroup(name: "name")

        when:
        def saved = service.save(group)

        then:
        saved instanceof TestGroup
    }

    void "save with invalid group throws validation exception"() {
        given:
        def group = new TestGroup()

        when:
        service.save(group)

        then:
        thrown(ValidationException)
    }
}
