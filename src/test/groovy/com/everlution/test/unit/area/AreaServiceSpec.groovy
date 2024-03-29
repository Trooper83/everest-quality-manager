package com.everlution.test.unit.area

import com.everlution.domains.Area
import com.everlution.services.area.AreaService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class AreaServiceSpec extends Specification implements ServiceUnitTest<AreaService>, DataTest {

    def setupSpec() {
        mockDomain(Area)
    }

    private void setupData() {
        new Area(name: "area 51").save()
        new Area(name: "area 51").save()
        new Area(name: "area 51").save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof Area
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }
}
