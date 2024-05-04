package com.everlution.test.unit.platform

import com.everlution.domains.Platform
import com.everlution.services.platform.PlatformService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class PlatformServiceSpec extends Specification implements ServiceUnitTest<PlatformService>, DataTest {

    def setupSpec() {
        mockDomain(Platform)
    }

    private void setupData() {
        new Platform(name: "area 51").save()
        new Platform(name: "area 51").save()
        new Platform(name: "area 51").save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof Platform
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "get with null does not throw exception"() {
        when:
        service.get(null)

        then:
        noExceptionThrown()
    }
}
