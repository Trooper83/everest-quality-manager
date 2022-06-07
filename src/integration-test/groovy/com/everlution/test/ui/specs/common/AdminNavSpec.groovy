package com.everlution.test.ui.specs.common

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class AdminNavSpec extends GebSpec {

    void "admin button hidden for basic and read only users"() {
        expect:
        false
    }

    void "admin button displayed for project admin and above"() {
        expect:
        false
    }

    void "user options displayed for app admin"() {
        expect:
        false
    }

    void "user options hidden for org admin and below"() {
        expect:
        false
    }

    void "project options displayed for project admin and above"() {
        expect:
        false
    }
}
