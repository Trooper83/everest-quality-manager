package com.manager.quality.everest.test.unit.security

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.services.person.PersonService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class PersonServiceSpec extends Specification implements ServiceUnitTest<PersonService>, DataTest {

    def setupSpec() {
        mockDomain(Person)
    }

    def setup() {
        new Person(email: "test@test.com", password: "!Password2022").save()
        new Person(email: "test1@test.com", password: "!Password2022").save()
        new Person(email: "test2@test.com", password: "!Password2022").save(flush: true)
    }

    void "find by email with valid email returns instance"() {
        expect: "valid instance"
        service.findByEmail("test@test.com") instanceof Person
    }

    void "get with invalid id returns null"() {
        expect:
        service.findByEmail("fail@fail.com") == null
    }

    void "list max args param returns correct value"() {
        expect:
        service.list(max: 1).size() == 1
        service.list(max: 2).size() == 2
        service.list().size() == 3
        service.list(offset: 1).size() == 2
    }
}
