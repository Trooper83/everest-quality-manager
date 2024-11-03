package com.manager.quality.everest.test.unit.testgroup

import com.manager.quality.everest.GroupWithPaginatedTests
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.controllers.TestGroupController
import com.manager.quality.everest.services.testgroup.TestGroupService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import spock.lang.*

class TestGroupControllerSpec extends Specification implements ControllerUnitTest<TestGroupController>, DomainUnitTest<TestGroup> {

    def populateValidParams(params) {
        assert params != null
        params.name = "test group name"
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/testCase/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "test groups action renders testGroups view"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:
        controller.testGroups(1, 10)

        then:
        view == 'testGroups'
    }

    void "testGroups action method type"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.testGroups(1,1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH"]
    }

    void "test groups action returns the correct model"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllByProject(_, params) >> new SearchResult([new TestGroup()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The index action is executed"
        controller.testGroups(1, 10)

        then:"The model is correct"
        model.testGroupList
        model.testGroupCount
        model.project
    }

    void "test groups action sets sort and order params when not found"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllByProject(_, params) >> new SearchResult([new TestGroup()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The index action is executed"
        controller.testGroups(1, 10)

        then:
        controller.params.sort == 'dateCreated'
        controller.params.order == 'desc'
    }

    void "test groups action sets sort and order params when not found"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllByProject(_, params) >> new SearchResult([new TestGroup()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The index action is executed"
        params.sort = 'a'
        params.order = 'b'
        controller.testGroups(1, 10)

        then:
        controller.params.sort == 'a'
        controller.params.order == 'b'
    }

    void "test groups action returns not found with invalid project"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            0 * findAllByProject(_) >> []
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.testGroups(null, 10)

        then:
        response.status == 404
    }

    void "testGroups search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllInProjectByName(project, 'test', params) >> new SearchResult([new TestGroup()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        params.isSearch = 'true'
        params.searchTerm = 'test'
        controller.testGroups(1, 10)

        then:"model is correct"
        model.testGroupList != null
        model.testGroupCount != null
        model.project != null
    }

    void "testGroups action param max"(Integer max, int expected) {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the action is executed"
        controller.testGroups(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
    }

    void "create action method type"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.create(1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "POST", "PATCH"]
    }

    void "create action returns create view"() {
        given: "mock service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:
        controller.create(1)

        then:
        view == "create"
    }

    void "create action returns the correct model"() {
        when:"The create action is executed"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }
        controller.create(1)

        then:"The model is correctly created"
        model.testGroup instanceof TestGroup
        model.project instanceof Project
    }

    void "create action throws not found when project is null"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The create action is executed"
        controller.create(null)

        then:"not found"
        response.status == 404
    }

    void "save action method type"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def group = new TestGroup(params)

        when:
        controller.save(group)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PUT"]
    }

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        setToken(params)
        controller.save(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "save responds 500 when no token present"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:
        response.status == 500
    }

    void "Test the save action correctly persists"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * save(_ as TestGroup)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        setToken(params)
        populateValidParams(params)
        def testGroup = new TestGroup(params)
        testGroup.id = 1
        def project = new Project()
        project.id = 1
        testGroup.project = project

        controller.save(testGroup)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/testGroup/show/1'
        controller.flash.message == "default.created.message"
    }

    void "save action with an invalid instance"() {
        given:
        def p = new Project()
        p.id = 1

        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> p
        }
        controller.testGroupService = Mock(TestGroupService) {
            1 * save(_ as TestGroup) >> { TestGroup testGroup ->
                throw new ValidationException("Invalid instance", testGroup.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        setToken(params)
        def testGroup = new TestGroup()
        testGroup.project = p
        controller.save(testGroup)

        then:"The create view is rendered again with the correct model"
        model.testGroup != null
        model.project == p
        view == 'create'
    }

    void "show action renders show view"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getWithPaginatedTests(2, params) >> new GroupWithPaginatedTests(new TestGroup(), [])
        }

        when:"a domain instance is passed to the show action"
        controller.show(2, null)

        then:
        view == "show"
    }

    void "show action with a null id"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getWithPaginatedTests(null, params) >> new GroupWithPaginatedTests(null, [])
        }

        when:"The show action is executed with a null domain"
        controller.show(null, null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action method type"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.show(1,1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "POST", "PATCH"]
    }

    void "show action with a valid id returns a test group and tests"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getWithPaginatedTests(2, params) >> new GroupWithPaginatedTests(new TestGroup(), [])
        }

        when:"A domain instance is passed to the show action"
        controller.show(2, 0)

        then:"A model is populated containing the domain instance"
        model.testGroup instanceof TestGroup
        model.tests != null
    }

    void "show action param max"(Integer max, int expected) {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * getWithPaginatedTests(1, params) >> new GroupWithPaginatedTests(new TestGroup(), [])
        }

        when:"the action is executed"
        controller.show(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
    }

    void "edit action with a null id returns 404"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "edit action method type"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.edit(1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "POST", "PATCH"]
    }

    void "edit action renders edit view"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * get(2) >> new TestGroup()
        }

        when:"a domain instance is passed to the show action"
        controller.edit(2)

        then:
        view == "edit"
    }

    void "edit action with a valid id returns a test group instance"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * get(2) >> new TestGroup()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.testGroup instanceof TestGroup
    }

    void "update action invalid method returns 405"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def group = new TestGroup(params)

        when:
        controller.update(group, null)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'POST'       | _
    }

    void "update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(null, 1)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update responds with 500 when no token present"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, 1)

        then:
        response.status == 500
    }

    void "update action with a valid test case instance and null projectId param returns 404"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(new TestGroup(), null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action with a projectId param not matching test case projectId returns 404"() {
        when:
        def testGroup = new TestGroup(params)
        testGroup.id = 1
        def project = new Project()
        project.id = 1
        setToken(params)
        testGroup.project = project
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(testGroup, 999)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action correctly persists"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * save(_ as TestGroup)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def testGroup = new TestGroup(params)
        testGroup.id = 1
        def project = new Project()
        project.id = 1
        testGroup.project = project

        controller.update(testGroup, 1)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/testGroup/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * save(_ as TestGroup) >> { TestGroup testGroup ->
                throw new ValidationException("Invalid instance", testGroup.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        def testGroup = new TestGroup(params)
        testGroup.id = 1
        def project = new Project()
        project.id = 1
        testGroup.project = project
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(testGroup, 1)

        then:"The edit view is rendered again with the correct model"
        model.testGroup != null
        view == 'edit'
    }

    void "delete action with invalid method returns 405"(String httpMethod) {
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

    void "delete action with a null instance redirects to index"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(null, 1)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete responds with 500 when no token present"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null, 1)

        then:
        response.status == 500
    }

    void "delete action with a null project returns 404"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(1, null)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action with an instance"() {
        def testGroup = new TestGroup(params)
        testGroup.id = 2
        def project = new Project()
        project.id = 1
        setToken(params)
        testGroup.project = project
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * delete(2)
            1 * read(2) >> testGroup
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"The user is redirected to index"
        response.redirectedUrl == '/project/1/testGroups'
        flash.message == "default.deleted.message"
    }

    void "delete action returns 404 with test case project not matching params project"() {
        given:
        def testGroup = new TestGroup(params)
        testGroup.id = 2
        def project = new Project()
        project.id = 1
        setToken(params)
        testGroup.project = project

        controller.testGroupService = Mock(TestGroupService) {
            1 * read(2) >> testGroup
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 999)

        then:"A 404 is returned"
        response.status == 404
    }
}






