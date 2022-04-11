package com.everlution.test.testgroup

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestGroup
import com.everlution.TestGroupController
import com.everlution.TestGroupService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class TestGroupControllerSpec extends Specification implements ControllerUnitTest<TestGroupController>, DomainUnitTest<TestGroup> {

    def populateValidParams(params) {
        assert params != null
        params.name = "test group name"
    }

    void "test groups action renders index view"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllByProject(_) >> []
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:
        controller.testGroups(1)

        then:
        view == 'testGroups'
    }

    void "test groups action returns the correct model"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * findAllByProject(_) >> [new TestGroup()]
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The index action is executed"
        controller.testGroups(1)

        then:"The model is correct"
        model.testGroupList
        model.testGroupCount
        model.project
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
        controller.testGroups(null)

        then:
        response.status == 404
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
        controller.save(null)

        then:"A 404 error is returned"
        response.status == 404
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
        controller.testGroupService = Mock(TestGroupService) {
            1 * save(_ as TestGroup) >> { TestGroup testGroup ->
                throw new ValidationException("Invalid instance", testGroup.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def testGroup = new TestGroup()
        controller.save(testGroup)

        then:"The create view is rendered again with the correct model"
        model.testGroup != null
        view == 'create'
    }

    void "show action renders show view"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * get(2) >> new TestGroup()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "Test the show action with a null id"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action with a valid id returns a test group"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * get(2) >> new TestGroup()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.testGroup instanceof TestGroup
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
        controller.update(group)

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
        controller.update(null)

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
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def testGroup = new TestGroup(params)
        testGroup.id = 1
        def project = new Project()
        project.id = 1
        testGroup.project = project

        controller.update(testGroup)

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
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new TestGroup())

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
        controller.delete(null, 1)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action with a null project returns 404"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(1, null)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action with an instance"() {
        given:
        controller.testGroupService = Mock(TestGroupService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"The user is redirected to index"
        response.redirectedUrl == '/project/1/testGroups'
        flash.message == "default.deleted.message"
    }
}






