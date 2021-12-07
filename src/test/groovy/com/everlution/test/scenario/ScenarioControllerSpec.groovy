package com.everlution.test.scenario

import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioController
import com.everlution.ScenarioService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class ScenarioControllerSpec extends Specification implements ControllerUnitTest<ScenarioController>, DomainUnitTest<Scenario> {

    def populateValidParams(params) {
        assert params != null

        params.name = "unit test scenario name"
    }

    void "test index action renders index view"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when: "call index action"
        controller.index()

        then: "index view is returned"
        view == 'index'
    }

    void "index action returns the correct model"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"the index action is executed"
        controller.index()

        then:"the model is correct"
        !model.scenarioList
        model.scenarioCount == 0
    }

    void "index action param max"(Integer max, int expected) {
        given:
        controller.scenarioService = Mock(ScenarioService) {
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
        given: "mock service"
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        view == "create"
    }

    void "create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"create action is executed"
        controller.create()

        then:"model is correctly created"
        model.scenario!= null
    }

    void "save action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def scn = new Scenario(params)

        when:
        controller.save(scn)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PUT", "PATCH"]
    }

    void "save action with a null instance"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/scenario/index'
        flash.message == "default.not.found.message"
    }

    void "save action correctly persists"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario)
        }

        when:"the save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def scenario = new Scenario(params)
        scenario.id = 1

        controller.save(scenario)

        then:"redirect is issued to the show action"
        response.redirectedUrl == '/scenario/show/1'
        controller.flash.message == "default.created.message"
    }

    void "save action with an invalid instance"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario) >> { Scenario scenario ->
                throw new ValidationException("Invalid instance", scenario.errors)
            }
        }

        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"the save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def scenario = new Scenario()
        controller.save(scenario)

        then:"create view is rendered again with the correct model"
        model.scenario instanceof Scenario
        model.projects instanceof List
        view == 'create'
    }

    void "show action renders show view"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * get(2) >> new Scenario()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "show action with a null id"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * get(null) >> null
        }

        when:"show action is executed with a null domain"
        controller.show(null)

        then:"404 error is returned"
        response.status == 404
    }

    void "show action with a valid id"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * get(2) >> new Scenario()
        }

        when:"domain instance is passed to the show action"
        controller.show(2)

        then:"model is populated containing the domain instance"
        model.scenario instanceof Scenario
    }

    void "edit action with a null id"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * get(null) >> null
        }

        when:"show action is executed with a null domain"
        controller.edit(null)

        then:"404 error is returned"
        response.status == 404
    }

    void "edit action with a valid id"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * get(2) >> new Scenario()
        }

        when:"domain instance is passed to the show action"
        controller.edit(2)

        then:"model is populated containing the domain instance"
        model.scenario instanceof Scenario
    }

    void "edit action renders edit view"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * get(2) >> new Scenario()
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
        def scn = new Scenario(params)

        when:
        controller.update(scn)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "POST", "PATCH"]
    }

    void "update action with a null instance"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null)

        then:"404 error is returned"
        response.redirectedUrl == '/scenario/index'
        flash.message == "default.not.found.message"
    }

    void "update action correctly persists"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario)
        }

        when:"save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def scenario = new Scenario(params)
        scenario.id = 1

        controller.update(scenario)

        then:"redirect is issued to the show action"
        response.redirectedUrl == '/scenario/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario) >> { Scenario scenario ->
                throw new ValidationException("Invalid instance", scenario.errors)
            }
        }

        when:"save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new Scenario())

        then:"edit view is rendered again with the correct model"
        model.scenario instanceof Scenario
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

    void "delete action with a null instance"() {
        when:"delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"404 is returned"
        response.redirectedUrl == '/scenario/index'
        flash.message == "default.not.found.message"
    }

    void "delete action with an instance"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * delete(2)
        }

        when:"domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"user is redirected to index"
        response.redirectedUrl == '/scenario/index'
        flash.message == "default.deleted.message"
    }
}






