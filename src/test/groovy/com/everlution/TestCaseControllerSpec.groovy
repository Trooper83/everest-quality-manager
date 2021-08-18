package com.everlution

import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class TestCaseControllerSpec extends Specification implements ControllerUnitTest<TestCaseController>, DomainUnitTest<TestCase> {

    def populateValidParams(params) {
        assert params != null

        params.name = "unit testing name"
        params.description = "unit testing description"
    }

    void "test index action renders index view"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:
        controller.index()

        then:
        view == 'index'
    }

    void "test the index action returns the correct model"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"the index action is executed"
        controller.index()

        then:"the model is correct"
        !model.testCaseList
        model.testCaseCount == 0
    }

    void "test the index action param max"(Integer max, int expected) {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * list(_) >> []
        }

        when:"the index action is executed"
        controller.index(max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 10
        1    | 1
        99   | 99
        101  | 100
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
        model.testCase!= null
    }

    void "test the save action method"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def testCase = new TestCase(params)

        when:
        controller.save(testCase)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'PUT'        | _
    }

    void "test the save action with a null instance"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"a 404 error is returned"
        response.redirectedUrl == '/testCase/index'
        flash.message == "default.not.found.message"
    }

    void "test the save action correctly persists"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase)
        }

        when:"the save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCase = new TestCase(params)
        testCase.id = 1

        controller.save(testCase)

        then:"a redirect is issued to the show action"
        response.redirectedUrl == '/testCase/show/1'
        controller.flash.message == "default.created.message"
    }

    void "test the save action with an invalid instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase) >> { TestCase testCase ->
                throw new ValidationException("Invalid instance", testCase.errors)
            }
        }

        when:"the save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def testCase = new TestCase()
        controller.save(testCase)

        then:"the create view is rendered again with the correct model"
        model.testCase != null
        view == 'create'
    }

    void "test the show action renders show view"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(2) >> new TestCase()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "test the show action with a null id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(null) >> null
        }

        when:"the show action is executed with a null domain"
        controller.show(null)

        then:"a 404 error is returned"
        response.status == 404
    }

    void "test the show action with a valid id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(2) >> new TestCase()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:"a model is populated containing the domain instance"
        model.testCase instanceof TestCase
    }

    void "test the edit action with a null id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(null) >> null
        }

        when:"the show action is executed with a null domain"
        controller.edit(null)

        then:"a 404 error is returned"
        response.status == 404
    }

    void "test the edit action renders edit view"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(2) >> new TestCase()
        }

        when:"a domain instance is passed to the show action"
        controller.edit(2)

        then:
        view == "edit"
    }

    void "test the edit action with a valid id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(2) >> new TestCase()
        }

        when:"a domain instance is passed to the show action"
        controller.edit(2)

        then:"a model is populated containing the domain instance"
        model.testCase instanceof TestCase
    }

    void "test the update action method"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def testCase = new TestCase(params)

        when:
        controller.update(testCase)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'POST'       | _
    }

    void "test the update action with a null instance"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"a 404 error is returned"
        response.redirectedUrl == '/testCase/index'
        flash.message == "default.not.found.message"
    }

    void "test the update action correctly persists"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase)
        }

        when:
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def testCase = new TestCase(params)
        testCase.id = 1

        controller.update(testCase)

        then:"a redirect is issued to the show action"
        response.redirectedUrl == '/testCase/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "test the update action with an invalid instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase) >> { TestCase testCase ->
                throw new ValidationException("Invalid instance", testCase.errors)
            }
        }

        when:"the save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new TestCase())

        then:"the edit view is rendered again with the correct model"
        model.testCase != null
        view == 'edit'
    }

    void "test the delete action method"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.delete(1)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'PUT'        | _
        'POST'       | _
    }

    void "test the delete action with a null instance"() {
        when:"the delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"a 404 is returned"
        response.redirectedUrl == '/testCase/index'
        flash.message == "default.not.found.message"
    }

    void "test the delete action with an instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * delete(2)
        }

        when:"the domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"the user is redirected to index"
        response.redirectedUrl == '/testCase/index'
        flash.message == "default.deleted.message"
    }
}






