package com.everlution.test.unit.bug

import com.everlution.domains.Bug
import com.everlution.controllers.BugController
import com.everlution.services.bug.BugService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.SearchResult
import com.everlution.controllers.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import spock.lang.*

class BugControllerSpec extends Specification implements ControllerUnitTest<BugController>, DomainUnitTest<Bug> {

    def populateValidParams(params) {
        assert params != null

        params.name = "controller unit test bug"
        params.description = "this is the description"
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/bugController/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "bugs action param max"(Integer max, int expected) {
        given:
        controller.bugService = Mock(BugService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the action is executed"
        controller.bugs(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
    }

    void "bugs action renders bugs view"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call bugs action"
        controller.bugs(1, 10)

        then: "view is returned"
        view == 'bugs'
    }

    void "bugs action returns the correct model"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * findAllByProject(_, params) >> new SearchResult([new Bug()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The action is executed"
        controller.bugs(1, null)

        then:"The model is correct"
        model.bugList != null
        model.bugCount != null
        model.project != null
    }

    void "bugs action returns not found with invalid project"() {
        given:
        controller.bugService = Mock(BugService) {
            0 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.bugs(null, null)

        then:
        response.status == 404
    }

    void "bugs search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.bugService = Mock(BugService) {
            1 * findAllInProjectByName(project, 'test', params) >> new SearchResult([new Bug()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        setToken(params)
        params.isSearch = 'true'
        params.searchTerm = 'test'
        controller.bugs(1, 10)

        then:"model is correct"
        model.bugList != null
        model.bugCount != null
        model.project != null
    }

    void "bugs action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.bugs(null, null)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH"]
    }

    void "create action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.create(1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH", "POST"]
    }

    void "test the create action returns the correct view"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the create action is executed"
        controller.create(1)

        then:"the model is correctly created"
        view == "create"
    }

    void "create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The create action is executed"
        controller.create()

        then:"The model is correctly populated with bug and projects"
        model.bug instanceof Bug
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

    void "save action returns 405 with not allowed method types"(String httpMethod) {
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

    void "save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        setToken(params)
        controller.save(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "save responds with 500 when no token present"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 500 error is returned"
        response.status == 500
    }

    void "save action correctly persists"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * save(_ as Bug)
        }
        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        setToken(params)
        def bug = new Bug(params)
        def project = new Project()
        bug.id = 1
        project.id = 1
        bug.project = project

        controller.save(bug)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/bug/show/1'
        controller.flash.message == "default.created.message"
    }

    void "save action with an invalid instance"() {
        given: "mock services"
        def p = new Project()
        p.id = 1
        controller.bugService = Mock(BugService) {
            1 * save(_ as Bug) >> { Bug bug ->
                throw new ValidationException("Invalid instance", bug.errors)
            }
        }
        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> p
        }
        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }

        when:"The save action is executed with an invalid instance"
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def bug = new Bug()
        bug.project = p
        controller.save(bug)

        then:"The create view is rendered again with the correct model"
        model.bug instanceof Bug
        model.project == p
        view == 'create'
    }

    void "show action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.show(1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH", "POST"]
    }

    void "show action renders show view"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "show action with a null id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action with a valid id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.bug instanceof Bug
    }

    void "edit action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.edit(1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH", "POST"]
    }

    void "edit action with a null id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "edit action with a valid id"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.bug instanceof Bug
    }

    void "edit action renders edit view"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * get(2) >> new Bug()
        }

        when:"a domain instance is passed to the show action"
        controller.edit(2)

        then:
        view == "edit"
    }

    void "update action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.update(null, null, null)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PATCH", "POST"]
    }

    void "update action method"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def bug = new Bug(params)

        when:
        controller.update(bug, new RemovedItems(), null)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "POST", "PATCH"]
    }

    void "update action with a null bug instance returns 404"() {
        when:"update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(null, null, 1)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update responds with 500 when no token present"() {
        when:"update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, null, 1)

        then:
        response.status == 500
    }

    void "update action with a valid bug instance and null projectId param returns 404"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(new Bug(), null, null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action with a projectId param not matching bug projectId returns 404"() {
        when:
        def bug = new Bug()
        def project = new Project()
        project.id = 999
        bug.project = project
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(bug, null, 1)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "test the update action correctly persists"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * saveUpdate(_ as Bug, _ as RemovedItems)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def bug = new Bug(params)
        def project = new Project()
        bug.id = 1
        project.id = 1
        bug.project = project

        controller.update(bug, new RemovedItems(), 1)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/bug/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        controller.bugService = Mock(BugService) {
            1 * saveUpdate(_ as Bug, _ as RemovedItems) >> { Bug bug, RemovedItems removedItems ->
                throw new ValidationException("Invalid instance", bug.errors)
            }
            1 * read(_) >> {
                Mock(Bug)
            }
        }

        when:"update action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def bug = new Bug(params)
        def project = new Project()
        bug.id = 1
        project.id = 1
        bug.project = project
        controller.update(bug, new RemovedItems(), 1)

        then:"The edit view is rendered again with the correct model"
        model.bug instanceof Bug
        view == '/bug/edit'
    }

    void "delete action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.delete(1, 1)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "PUT", "PATCH", "POST"]
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

        then:"A 500 is returned"
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

    void "delete action returns 404 with bug project not matching params project"() {
        given:
        params.projectId = 999
        populateValidParams(params)
        def bug = new Bug(params)
        def project = new Project()
        bug.id = 2
        project.id = 1
        bug.project = project

        controller.bugService = Mock(BugService) {
            1 * read(2) >> bug
        }

        when:"The domain instance is passed to the delete action"
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 999)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action with an instance"() {
        given:
        params.projectId = 1
        populateValidParams(params)
        def bug = new Bug(params)
        def project = new Project()
        bug.id = 2
        project.id = 1
        bug.project = project

        controller.bugService = Mock(BugService) {
            1 * delete(2)
            1 * read(2) >> bug
        }

        when:"The domain instance is passed to the delete action"
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"The user is redirected to index"
        response.redirectedUrl == '/project/1/bugs'
        flash.message == "default.deleted.message"
    }
}






