package com.everlution.test.release

import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanController
import com.everlution.ReleasePlanService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class ReleasePlanControllerSpec extends Specification implements ControllerUnitTest<ReleasePlanController>, DomainUnitTest<ReleasePlan> {

    def populateValidParams(params) {
        assert params != null

        params.name = "test release plan"
    }

    void "index action renders index view"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:
        controller.index()

        then:
        view == 'index'
    }

    void "the index action returns the correct model"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.releasePlanList
        model.releasePlanCount == 0
    }

    void "index action param max"(Integer max, int expected) {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
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

    void "create action returns create view"() {
        given: "mock service"
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:
        controller.create()

        then:
        view == "create"
    }

    void "create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.releasePlan instanceof ReleasePlan
        model.projects instanceof List
    }

    void "save action method type"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def plan = new ReleasePlan(params)

        when:
        controller.save(plan)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'PUT'        | _
    }

    void "save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/releasePlan/index'
        flash.message == "default.not.found.message"
    }

    void "save action correctly persists"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * save(_ as ReleasePlan)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def releasePlan = new ReleasePlan(params)
        releasePlan.id = 1

        controller.save(releasePlan)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/releasePlan/show/1'
        controller.flash.message == "default.created.message"
    }

    void "save action with an invalid instance"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * save(_ as ReleasePlan) >> { ReleasePlan releasePlan ->
                throw new ValidationException("Invalid instance", releasePlan.errors)
            }
        }
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def releasePlan = new ReleasePlan()
        controller.save(releasePlan)

        then:"The create view is rendered again with the correct model"
        model.releasePlan != null
        model.projects != null
        view == 'create'
    }

    void "show action renders show view"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(2) >> new ReleasePlan()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "show action with a null id"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action with a valid id returns ReleasePlan instance"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(2) >> new ReleasePlan()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.releasePlan instanceof ReleasePlan
    }

    void "edit action with a null id returns 404"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "edit action renders edit view"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(2) >> new ReleasePlan()
        }

        when:"a domain instance is passed to the show action"
        controller.edit(2)

        then:
        view == "edit"
    }

    void "edit action with a valid id returns a ReleasePlan instance"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * get(2) >> new ReleasePlan()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.releasePlan instanceof ReleasePlan
    }

    void "update action invalid method returns 405"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def plan = new ReleasePlan(params)

        when:
        controller.update(plan)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'POST'       | _
    }

    void "update action with a null instance returns 404"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"redirected to index"
        response.redirectedUrl == '/releasePlan/index'
        flash.message == "default.not.found.message"
    }

    void "update action correctly persists"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * save(_ as ReleasePlan)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def releasePlan = new ReleasePlan(params)
        releasePlan.id = 1

        controller.update(releasePlan)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/releasePlan/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * save(_ as ReleasePlan) >> { ReleasePlan releasePlan ->
                throw new ValidationException("Invalid instance", releasePlan.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new ReleasePlan())

        then:"The edit view is rendered again with the correct model"
        model.releasePlan != null
        view == 'edit'
    }

    void "delete action with invalid method returns 405"(String httpMethod) {
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

    void "delete action with a null instance returns 404"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"redirect to index"
        response.redirectedUrl == '/releasePlan/index'
        flash.message == "default.not.found.message"
    }

    void "delete action with an instance redirects to index"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/releasePlan/index'
        flash.message == "default.deleted.message"
    }
}






