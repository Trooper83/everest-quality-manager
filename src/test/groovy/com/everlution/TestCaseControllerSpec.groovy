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

    void "Test the index action returns the correct model"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.testCaseList
        model.testCaseCount == 0
    }

    void "Test the index action param max"(Integer max, int expected) {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * list(_) >> []
        }

        when:"The index action is executed"
        controller.index(max)

        then:"The max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 10
        1    | 1
        99   | 99
        101  | 100
    }

    void "Test the create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.testCase!= null
    }

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/testCase/index'
        flash.message == "default.not.found.message"
    }

    void "Test the save action correctly persists"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCase = new TestCase(params)
        testCase.id = 1

        controller.save(testCase)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/testCase/show/1'
        controller.flash.message == "default.created.message"
    }

    void "Test the save action with an invalid instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase) >> { TestCase testCase ->
                throw new ValidationException("Invalid instance", testCase.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def testCase = new TestCase()
        controller.save(testCase)

        then:"The create view is rendered again with the correct model"
        model.testCase != null
        view == 'create'
    }

    void "Test the show action with a null id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the show action with a valid id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(2) >> new TestCase()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.testCase instanceof TestCase
    }

    void "Test the edit action with a null id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the edit action with a valid id"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * get(2) >> new TestCase()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.testCase instanceof TestCase
    }


    void "Test the update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/testCase/index'
        flash.message != null
    }

    void "Test the update action correctly persists"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def testCase = new TestCase(params)
        testCase.id = 1

        controller.update(testCase)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/testCase/show/1'
        controller.flash.message != null
    }

    void "Test the update action with an invalid instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase) >> { TestCase testCase ->
                throw new ValidationException("Invalid instance", testCase.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new TestCase())

        then:"The edit view is rendered again with the correct model"
        model.testCase != null
        view == 'edit'
    }

    void "Test the delete action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A 404 is returned"
        response.redirectedUrl == '/testCase/index'
        flash.message == "default.not.found.message"
    }

    void "Test the delete action with an instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/testCase/index'
        flash.message == "default.deleted.message"
    }
}






