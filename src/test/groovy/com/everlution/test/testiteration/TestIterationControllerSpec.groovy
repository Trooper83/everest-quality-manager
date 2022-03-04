package com.everlution.test.testiteration

import com.everlution.Bug
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.TestIteration
import com.everlution.TestIterationController
import com.everlution.TestIterationService
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
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

    void "execute action renders execute view"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"a domain instance is passed to the execute action"
        controller.execute(2)

        then:
        view == "execute"
    }

    void "execute action with null id returns 404"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(null) >> null
        }

        when:"The execute action is executed with a null domain"
        controller.execute(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "execute action with a valid id returns a test iteration"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"A domain instance is passed to the execute action"
        controller.execute(2)

        then:"A model is populated containing the domain instance"
        model.testIteration instanceof TestIteration
    }

    void "update returns 404 with null id"() {
        when:
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:
        response.status == 404
    }

    void "update action invalid method returns 405"(String httpMethod) {
        given:
        request.method = httpMethod
        def iteration = new TestIteration()

        when:
        controller.update(iteration)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'POST'       | _
    }

    void "update action correctly persists"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * save(_ as TestIteration)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        def iteration = new TestIteration()
        iteration.id = 1

        controller.update(iteration)

        then:"A redirect is issued to the show action"
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        def iteration = new TestIteration()
        controller.testIterationService = Mock(TestIterationService) {
            1 * save(_ as TestIteration) >> { TestIteration testIteration ->
                throw new ValidationException("Invalid instance", testIteration.errors)
            }
            1 * read(_) >> {
                Mock(TestIteration)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(iteration)

        then:"The edit view is rendered again with the correct model"
        model.testIteration != null
        view == '/testIteration/execute'
    }
}
