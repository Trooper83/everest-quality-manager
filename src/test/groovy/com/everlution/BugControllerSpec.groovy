package com.everlution

import com.everlution.command.RemovedItems
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class BugControllerSpec extends Specification implements ControllerUnitTest<BugController>, DomainUnitTest<Bug> {

    def populateValidParams(params) {
        assert params != null

        params.name = "controller unit test bug"
        params.description = "this is the description"
    }

    void "test index action renders index view"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when: "call index action"
        controller.index()

        then: "index view is returned"
        view == 'index'
    }

    void "Test the index action returns the correct model"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.bugList
        model.bugCount == 0
    }

    void "test the index action param max"(Integer max, int expected) {
        given:
        controller.bugService = Mock(BugService) {
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
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        view == "create"
    }

    void "Test the create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"The create action is executed"
        controller.create()

        then:"The model is correctly populated with bug and projects"
        model.bug instanceof Bug
        model.projects instanceof List
    }

    void "test save action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def bug = new Bug(params)

        when:
        controller.save(bug)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PUT", "PATCH"]
    }

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/bug/index'
        flash.message == "default.not.found.message"
    }

    void "Test the save action correctly persists"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * save(_ as Bug)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def bug = new Bug(params)
        bug.id = 1

        controller.save(bug)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/bug/show/1'
        controller.flash.message == "default.created.message"
    }

    void "Test the save action with an invalid instance"() {
        given: "mock services"
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }
        controller.bugService = Mock(BugService) {
            1 * save(_ as Bug) >> { Bug bug ->
                throw new ValidationException("Invalid instance", bug.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def bug = new Bug()
        controller.save(bug)

        then:"The create view is rendered again with the correct model"
        model.bug instanceof Bug
        model.projects instanceof List
        view == 'create'
    }

    void "test the show action renders show view"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "Test the show action with a null id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the show action with a valid id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.bug instanceof Bug
    }

    void "Test the edit action with a null id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the edit action with a valid id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.bug instanceof Bug
    }

    void "test the edit action renders edit view"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"a domain instance is passed to the show action"
        controller.edit(2)

        then:
        view == "edit"
    }

    void "test the update action method"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def bug = new Bug(params)

        when:
        controller.update(bug, new RemovedItems())

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "POST", "PATCH"]
    }

    void "Test the update action with a null instance"() {
        when:"update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/bug/index'
        flash.message == "default.not.found.message"
    }

    void "Test the update action correctly persists"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * saveUpdate(_ as Bug, _ as RemovedItems)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def bug = new Bug(params)
        bug.id = 1

        controller.update(bug, new RemovedItems())

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/bug/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "Test the update action with an invalid instance"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * saveUpdate(_ as Bug, _ as RemovedItems) >> { Bug bug, RemovedItems removedItems ->
                throw new ValidationException("Invalid instance", bug.errors)
            }
        }

        when:"The saveUpdate action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new Bug(), new RemovedItems())

        then:"The edit view is rendered again with the correct model"
        model.bug instanceof Bug
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
        httpMethod << ["GET", "PUT", "POST", "PATCH"]
    }

    void "Test the delete action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A 404 is returned"
        response.redirectedUrl == '/bug/index'
        flash.message == "default.not.found.message"
    }

    void "Test the delete action with an instance"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/bug/index'
        flash.message == "default.deleted.message"
    }
}






