package com.everlution.test.scenario

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Scenario
import com.everlution.ScenarioController
import com.everlution.ScenarioService
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import spock.lang.*

class ScenarioControllerSpec extends Specification implements ControllerUnitTest<ScenarioController>, DomainUnitTest<Scenario> {

    def populateValidParams(params) {
        assert params != null

        params.name = "unit test scenario name"
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/scenario/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "scenarios action renders bugs view"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * findAllByProject(_) >> []
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call action"
        controller.scenarios(1)

        then: "view is returned"
        view == 'scenarios'
    }

    void "scenarios action returns the correct model"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * findAllByProject(_) >> [new Scenario()]
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"action is executed"
        controller.scenarios(1)

        then:"model is correct"
        model.scenarioList
        model.scenarioCount
        model.project
    }

    void "scenarios action returns not found with invalid project"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            0 * findAllByProject(_) >> []
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.scenarios()

        then:
        response.status == 404
    }

    void "scenarios search responds 500 when no token present"() {
        given:
        def project = new Project(name: 'test')
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"The action is executed"
        params.isSearch = 'true'
        params.name = 'test'
        controller.scenarios(1)

        then:
        response.status == 500
    }

    void "scenarios search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.scenarioService = Mock(ScenarioService) {
            1 * findAllInProjectByName(project, 'test') >> [new Scenario()]
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        setToken(params)
        params.isSearch = 'true'
        params.name = 'test'
        controller.scenarios(1)

        then:"model is correct"
        model.scenarioList != null
        model.scenarioCount != null
        model.project != null
    }

    void "test the create action returns the correct view"() {
        given: "mock service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        view == "create"
    }

    void "create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"create action is executed"
        controller.create()

        then:"model is correctly created"
        model.scenario
        model.project
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
        setToken(params)
        controller.save(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "save responds 500 when no token present"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:
        response.status == 500
    }

    void "save action correctly persists"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario)
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
        def scenario = new Scenario(params)
        def project = new Project()
        scenario.id = 1
        project.id = 1
        scenario.project = project

        controller.save(scenario)

        then:"redirect is issued to the show action"
        response.redirectedUrl == '/project/1/scenario/show/1'
        controller.flash.message == "default.created.message"
    }

    void "save action with an invalid instance"() {
        given:
        def p = new Project()
        p.id = 1

        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> p
        }
        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario) >> { Scenario scenario ->
                throw new ValidationException("Invalid instance", scenario.errors)
            }
        }

        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }

        when:"the save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        setToken(params)
        request.method = 'POST'
        def scenario = new Scenario()
        scenario.project = p
        controller.save(scenario)

        then:"create view is rendered again with the correct model"
        model.scenario instanceof Scenario
        model.project == p
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
        controller.update(scn, null)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "POST", "PATCH"]
    }

    void "update action with a null instance"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(null, 1)

        then:"404 error is returned"
        response.status == 404
    }

    void "update responds with 500 when no token present"() {
        when:"save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, 1)

        then:
        response.status == 500
    }

    void "update action with a valid scenario instance and null projectId param returns 404"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(new Scenario(), null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action with a projectId param not matching scenario projectId returns 404"() {
        when:
        def scenario = new Scenario(params)
        def project = new Project()
        scenario.id = 1
        project.id = 1
        scenario.project = project
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(scenario, 999)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action correctly persists"() {
        given:
        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario)
        }

        when:"save action is executed with a valid instance"
        response.reset()
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def scenario = new Scenario(params)
        def project = new Project()
        scenario.id = 1
        project.id = 1
        scenario.project = project

        controller.update(scenario, 1)

        then:"redirect is issued to the show action"
        response.redirectedUrl == '/project/1/scenario/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        def scenario = new Scenario(params)
        def project = new Project()
        scenario.id = 1
        project.id = 1
        scenario.project = project

        controller.scenarioService = Mock(ScenarioService) {
            1 * save(_ as Scenario) >> { Scenario scn ->
                throw new ValidationException("Invalid instance", scn.errors)
            }
            1 * read(_) >> scenario
        }

        when:"save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        setToken(params)
        request.method = 'PUT'
        controller.update(scenario, 1)

        then:"edit view is rendered again with the correct model"
        model.scenario instanceof Scenario
        view == '/scenario/edit'
    }

    void "test the delete action method"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.delete(1, 1)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "PUT", "POST", "PATCH"]
    }

    void "delete action with a null instance"() {
        when:"delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(null, 1)

        then:"404 is returned"
        response.status == 404
    }

    void "delete responds with 500 when no token present"() {
        when:"delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null, 1)

        then:
        response.status == 500
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

    void "delete action with an instance"() {
        given:
        def scenario = new Scenario(params)
        def project = new Project()
        scenario.id = 1
        project.id = 1
        scenario.project = project

        controller.scenarioService = Mock(ScenarioService) {
            1 * delete(2)
            1 * read(2) >> scenario
        }

        when:"domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        setToken(params)
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"user is redirected to index"
        response.redirectedUrl == '/project/1/scenarios'
        flash.message == "default.deleted.message"
    }

    void "delete action returns 404 with scenario project not matching params project"() {
        given:
        populateValidParams(params)
        def scenario = new Scenario(params)
        def project = new Project()
        scenario.id = 2
        project.id = 2
        scenario.project = project

        controller.scenarioService = Mock(ScenarioService) {
            1 * read(2) >> scenario
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        setToken(params)
        request.method = 'DELETE'
        controller.delete(2, 999)

        then:"A 404 is returned"
        response.status == 404
    }
}






