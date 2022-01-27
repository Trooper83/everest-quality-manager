package com.everlution.test.testcycle

import com.everlution.TestCycle
import com.everlution.TestCycleController
import com.everlution.TestCycleService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class TestCycleControllerSpec extends Specification implements ControllerUnitTest<TestCycleController>, DomainUnitTest<TestCycle> {

    def populateValidParams(params) {
        assert params != null

        params["name"] = 'someValidName'
        params["id"] = '3'
    }

    void "test the create action returns the correct view"() {
        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        view == "create"
    }

    void "test the create action returns the correct model"() {
        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        model.testCycle!= null
    }

    void "save action with a null instance"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        controller.save(null)

        then:"404 error is returned"
        response.redirectedUrl == '/releasePlan/show/3'
        flash.message != null
    }

    void "save action correctly persists"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * save(_ as TestCycle)
        }

        when:"save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCycle = new TestCycle(params)
        testCycle.id = 1

        controller.save(testCycle)

        then:"redirect is issued to the show action"
        response.redirectedUrl == '/releasePlan/show/3'
        controller.flash.message != null
    }

    void "save action with an invalid instance"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * save(_ as TestCycle) >> { TestCycle testCycle ->
                throw new ValidationException("Invalid instance", testCycle.errors)
            }
        }

        when:"save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def testCycle = new TestCycle()
        controller.save(testCycle)

        then:"create view is rendered again with the correct model"
        model.testCycle != null
        view == 'create'
    }

    void "save action method type"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def cycle = new TestCycle(params)

        when:
        controller.save(cycle)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'PUT'        | _
    }

    void "show action with a null id"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(null) >> null
        }

        when:"show action is executed with a null domain"
        controller.show(null)

        then:"404 error is returned"
        response.status == 404
    }

    void "show action with a valid id"() {
        given:
        controller.testCycleService = Mock(TestCycleService) {
            1 * get(2) >> new TestCycle()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.testCycle instanceof TestCycle
        view == 'show'
    }
}






