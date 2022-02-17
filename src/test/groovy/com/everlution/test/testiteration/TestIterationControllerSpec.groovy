package com.everlution.test.testiteration

import com.everlution.TestIteration
import com.everlution.TestIterationController
import com.everlution.TestIterationService
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class TestIterationControllerSpec extends Specification implements ControllerUnitTest<TestIterationController> {

    void "show action renders show view"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "show action with null id returns 404"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action with a valid id returns a test iteration"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.testIteration instanceof TestIteration
    }
}
