package com.everlution.test.unit.environment

import com.everlution.domains.Environment
import com.everlution.services.environment.EnvironmentService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class EnvironmentServiceSpec extends Specification implements ServiceUnitTest<EnvironmentService>, DataTest {

    def setupSpec() {
        mockDomain(Environment)
    }

    private void setupData() {
        new Environment(name: "env 51").save()
        new Environment(name: "env 51").save()
        new Environment(name: "env 51").save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof Environment
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }
}
