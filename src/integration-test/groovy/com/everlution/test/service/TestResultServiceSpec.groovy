package com.everlution.test.service

import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class TestResultServiceSpec extends Specification {

    void "save returns instance"() {
        expect:
        false
    }
}
