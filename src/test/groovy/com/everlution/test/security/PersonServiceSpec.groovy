package com.everlution.test.security

import com.everlution.Person
import com.everlution.PersonService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class PersonServiceSpec extends Specification implements ServiceUnitTest<PersonService>, DataTest {

    def setupSpec() {
        mockDomain(Person)
    }

    def setup() {
        new Person(email: "test@test.com", password: "password").save()
        new Person(email: "test1@test.com", password: "password").save()
        new Person(email: "test2@test.com", password: "password").save(flush: true)
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
