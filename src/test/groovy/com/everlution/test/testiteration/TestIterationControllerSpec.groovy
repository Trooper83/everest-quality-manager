package com.everlution.test.testiteration

import com.everlution.TestIteration
import com.everlution.TestIterationController
import com.everlution.TestIterationService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.*

class TestIterationControllerSpec extends Specification implements ControllerUnitTest<TestIterationController>, DomainUnitTest<TestIteration> {

    void "show action with a null id"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(null) >> null
        }

        when:"show action is executed with a null domain"
        controller.show(null)

        then:"404 error is returned"
        response.status == 404
    }

    void "show action with a valid id"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"domain instance is passed to the show action"
        controller.show(2)

        then:"model is populated containing the domain instance"
        model.testIteration instanceof TestIteration
        view == 'show'
    }
}






