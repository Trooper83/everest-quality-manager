package com.everlution.test.testcase

import com.everlution.Person
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.SearchResult
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestCaseController
import com.everlution.TestCaseService
import com.everlution.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.*

class TestCaseControllerSpec extends Specification implements ControllerUnitTest<TestCaseController>, DomainUnitTest<TestCase> {

    def populateValidParams(params) {
        assert params != null

        params.name = "unit testing name"
        params.description = "unit testing description"
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/testCase/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "test cases action renders index view"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:
        controller.testCases(1, null)

        then:
        view == 'testCases'
    }

    void "test cases action returns the correct model"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllByProject(_, params) >>new SearchResult([new TestCase()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the index action is executed"
        controller.testCases(1, null)

        then:"the model is correct"
        model.testCaseList
        model.project
        model.testCaseCount
    }

    void "test cases action returns not found with invalid project"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            0 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.testCases(null, 10)

        then:
        response.status == 404
    }

    void "test cases search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllInProjectByName(project, 'test', params) >> new SearchResult([new TestCase()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        params.isSearch = 'true'
        setToken(params)
        params.searchTerm = 'test'
        controller.testCases(1, 10)

        then:"model is correct"
        model.testCaseList != null
        model.testCaseCount != null
        model.project != null
    }

    void "testCases action param max"(Integer max, int expected) {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the action is executed"
        controller.testCases(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
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
        setToken(params)
        controller.save(null)

        then:"a 404 error is returned"
        response.status == 404
    }

    void "save responds with 500 when no token present"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:
        response.status == 500
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
        setToken(params)
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

    void "save action sets person property"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * save(_ as TestCase)
        }
        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser() >> new Person(email: "testing@testing.com")
        }

        response.reset()
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCase = new TestCase(params)
        def project = new Project()
        project.id = 1
        testCase.id = 1
        testCase.project = project

        expect:
        testCase.person == null

        when:"the save action is executed with a valid instance"
        controller.save(testCase)

        then:"person is added to testcase"
        testCase.person.email == "testing@testing.com"
    }

    void "test the save action with an invalid instance"() {
        given:
        def p = new Project()
        p.id = 1

        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> p
        }
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
        setToken(params)
        def testCase = new TestCase()
        testCase.project = p
        controller.save(testCase)

        then:"the create view is rendered again with the correct model"
        model.testCase != null
        model.project == p
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
        controller.update(testCase, new RemovedItems(), null)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'POST'       | _
    }

    void "update action with a null instance returns 404"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(null, null, 999)

        then:"a 404 error is returned"
        response.status == 404
    }

    void "update responds with 500 when no token present"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, null, 999)

        then:
        response.status == 500
    }

    void "update action with a valid test case instance and null projectId param returns 404"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(new TestCase(), null, null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action with a projectId param not matching test case projectId returns 404"() {
        when:
        def testCase = new TestCase()
        def project = new Project()
        project.id = 999
        testCase.project = project
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(testCase, null, 1)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "test the update action correctly persists"() {
        given:
        controller.testCaseService = Mock(TestCaseService) {
            1 * saveUpdate(_ as TestCase, _ as RemovedItems)
        }

        when:
        response.reset()
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def testCase = new TestCase(params)
        def project = new Project()
        project.id = 1
        testCase.id = 1
        testCase.project = project

        controller.update(testCase, new RemovedItems(), 1)

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
        def testCase = new TestCase(params)
        def project = new Project()
        project.id = 1
        testCase.id = 1
        testCase.project = project
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(testCase, new RemovedItems(), 1)

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
        setToken(params)
        controller.delete(null, 1)

        then:"a 404 is returned"
        response.status == 404
    }

    void "delete responds with 500 when no token present"() {
        when:"the delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null, 1)

        then:
        response.status == 500
    }

    void "test the delete action with an instance"() {
        given:
        params.projectId = '1'
        def testCase = new TestCase(params)
        def project = new Project()
        project.id = 1
        testCase.id = 1
        testCase.project = project

        controller.testCaseService = Mock(TestCaseService) {
            1 * delete(2)
            1 * read(2) >> testCase
        }

        when:"the domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(2, 1)

        then:
        response.redirectedUrl == '/project/1/testCases'
        flash.message == "default.deleted.message"
    }

    void "delete action with a null project"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(1, null)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action returns 404 with test case project not matching params project"() {
        given:
        params.projectId = 999
        populateValidParams(params)
        def testCase = new TestCase(params)
        def project = new Project()
        testCase.id = 2
        project.id = 1
        testCase.project = project

        controller.testCaseService = Mock(TestCaseService) {
            1 * read(2) >> testCase
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(2, 999)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action flash message correct when exception thrown"() {
        given:
        params.projectId = '1'
        def testCase = new TestCase(params)
        def project = new Project()
        project.id = 1
        testCase.id = 2
        testCase.project = project

        controller.testCaseService = Mock(TestCaseService) {
            1 * delete(2) >> {
                throw new DataIntegrityViolationException("Exception")
            }
            1 * get(2) >> testCase
            1 * read(2) >> testCase
        }

        when:"the domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(2, 1)

        then:"the user is redirected to index"
        model.testCase != null
        flash.error == "Test Case has associated Test Iterations and cannot be deleted"
    }
}






