package com.everlution.test.ui.functional.specs.testcase.create

import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreatePageBuilderStepsSpec extends GebSpec {

    void "suggestions only displayed for string of 3 characters or more"() {
        expect:
        false
    }

    void "steps are retrieved when validation fails"() {
        expect:
        false
    }

    void "last step has remove link and can be removed"() {
        expect:
        false
    }

    void "adding a step removes the remove link"() {
        expect:
        false
    }

    void "removing a step adds a remove link to previous step"() {
        expect:
        false
    }

    void "changing step type resets builder form"() {
        expect:
        false
    }

    void "selecting suggested result displays properties and suggested steps"() {
        expect:
        false
    }

    void "selecting suggested step displays properties and suggested steps"() {
        expect:
        false
    }

    void "previous step name is displayed"() {
        expect:
        false
    }

    void "form is reset when last step is removed"() {
        expect:
        false
    }

    void "suggested steps name and properties are repopulated with current step when step removed"() {
        expect:
        false
    }

    void "suggestion results are removed when clicked outside of menu"() {
        expect:
        false
    }

    void "no results message displayed when no related steps found"() {
        expect:
        false
    }
}
