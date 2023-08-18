package com.everlution.test.ui.functional.specs.step

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ListSpec extends GebSpec {

    void "verify list table headers order"() {
        expect:
        false
    }

    void "clicking name column directs to show page"() {
        expect:
        false
    }

    void "search returns results"() {
        expect:
        false
    }

    void "delete message displays after scenario deleted"() {
        expect:
        false
    }

    void "search text field retains search value"() {
        expect:
        false
    }

    void "reset button reloads results"() {
        expect:
        false
    }
}
