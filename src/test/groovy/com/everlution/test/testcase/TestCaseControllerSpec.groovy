package com.everlution.test.testcase

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseController
import com.everlution.TestCaseService
import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.*

class TestCaseControllerSpec extends Specification implements ControllerUnitTest<TestCaseController>, DomainUnitTest<TestCase> {

    def populateValidParams(params) {
        assert params != null

        params.name = "unit testing name"
        params.description = "unit testing description"
    }

    void "test cases action renders index view"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllByProject(_) >> []
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:
        controller.testCases(1)

        then:
        view == 'testCases'
    }

    void "test cases action returns the correct model"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllByProject(_) >> [new TestCase()]
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the index action is executed"
        controller.testCases(1)

        then:"the model is correct"
        model.testCaseList
        model.project
        model.testCaseCount
    }

    void "test cases action returns not found with invalid project"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            0 * findAllByProject(_) >> []
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.testCases()

        then:
        response.status == 404
    }

    void "create action returns the correct view"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the create action is executed"
        controller.create(1)

        then:"the model is correctly created"
        view == "create"
    }

    void "test the create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        model.testCase instanceof TestCase
        model.project instanceof Project
    }

    void "create action throws not found when project is null"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The create action is executed"
        controller.create(1)

        then:"not found"
        response.status == 404
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
        response.status == 404
    }

    void "test the save action correctly persists"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase)
        }
        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }

        when:"the save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCase = new TestCase(params)
        def project = new Project()
        project.id = 1
        testCase.id = 1
        testCase.project = project

        controller.save(testCase)

        then:"a redirect is issued to the show action"
        response.redirectedUrl == '/project/1/testCase/show/1'
        controller.flash.message == "default.created.message"
    }

    void "test the save action with an invalid instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase) >> { TestCase testCase ->
                throw new ValidationException("Invalid instance", testCase.errors)
            }
        }
        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
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
        controller.update(testCase, new RemovedItems())

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
        controller.update(null, null)

        then:"a 404 error is returned"
        response.status == 404
    }

    void "test the update action correctly persists"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * saveUpdate(_ as TestCase, _ as RemovedItems)
        }

        when:
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def testCase = new TestCase(params)
        def project = new Project()
        project.id = 1
        testCase.id = 1
        testCase.project = project

        controller.update(testCase, new RemovedItems())

        then:"a redirect is issued to the show action"
        response.redirectedUrl == '/project/1/testCase/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "test the update action with an invalid instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * saveUpdate(_ as TestCase, _ as RemovedItems) >> { TestCase testCase, RemovedItems removedItems ->
                throw new ValidationException("Invalid instance", testCase.errors)
            }
            1 * read(_) >> {
                Mock(TestCase)
            }
        }

        when:"the action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new TestCase(), new RemovedItems())

        then:"the edit view is rendered again with the correct model"
        model.testCase != null
        view == '/testCase/edit'
    }

    void "test the delete action method"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.delete(1, 1)

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
        controller.delete(null, 1)

        then:"a 404 is returned"
        response.status == 404
    }

    void "test the delete action with an instance"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * delete(2)
        }

        when:"the domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:
        response.redirectedUrl == '/project/1/testCases'
        flash.message == "default.deleted.message"
    }

    void "delete action with a null project"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(1, null)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action flash message correct when exception thrown"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * delete(2) >> {
                throw new DataIntegrityViolationException("Exception")
            }
            1 * get(2) >> new TestCase()
        }

        when:"the domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"the user is redirected to index"
        model.testCase != null
        flash.error == "Test Case has associated Test Iterations and cannot be deleted"
    }
}






