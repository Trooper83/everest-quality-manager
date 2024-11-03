package com.manager.quality.everest.test.unit.project

import com.manager.quality.everest.services.automatedtest.AutomatedTestService
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.controllers.ProjectController
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.ReleasePlan
import com.manager.quality.everest.services.releaseplan.ReleasePlanService
import com.manager.quality.everest.services.scenario.ScenarioService
import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.controllers.command.RemovedItems
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.*

import java.time.Instant
import java.time.temporal.ChronoUnit

class ProjectControllerSpec extends Specification implements ControllerUnitTest<ProjectController>, DomainUnitTest<Project> {

    private Date pastDate = new Date().from(Instant.now().minus(5, ChronoUnit.DAYS))
    private Date futureDate = new Date().from(Instant.now().plus(5, ChronoUnit.DAYS))

    def populateValidParams(params) {
        assert params != null

        params.name = "unit test project"
        params.code = "SOX"
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/project/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "test the create action returns the correct view"() {
        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        view == "create"
    }

    void "test the create action returns the correct model"() {
        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.project != null
    }

    void "create action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.create()

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH", "POST"]
    }

    void "test save action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def project = new Project(params)

        when:
        controller.save(project)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PUT", "PATCH"]
    }

    void "test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        setToken(params)
        controller.save(null)

        then:"A 404 error is returned"
        flash.message == "default.not.found.message"
    }

    void "save responds with 500 with invalid token"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 500 error is returned"
        response.status == 500
    }

    void "test the save action correctly persists"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * save(_ as Project)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def project = new Project(params)
        project.id = 1

        controller.save(project)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/show/1'
        controller.flash.message == "default.created.message"
    }

    void "test the save action with an invalid instance"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * save(_ as Project) >> { Project project ->
                throw new ValidationException("Invalid instance", project.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        setToken(params)
        def project = new Project()
        controller.save(project)

        then:"The create view is rendered again with the correct model"
        model.project != null
        view == 'create'
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
        controller.projectService = Mock(ProjectService) {
            1 * get(null) >> null
        }

        when:"show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "edit action with a valid id"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> new Project()
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.project instanceof Project
    }

    void "test the edit action renders edit view"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> new Project()
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
        controller.update(null, null)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PATCH", "POST"]
    }

    void "test the update action method"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def project = new Project(params)

        when:
        controller.update(project, null)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "POST", "PATCH"]
    }

    void "updated responds with 500 with invalid token"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, null)

        then:"A 500 error is returned"
        response.status == 500
    }

    void "update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(null, null)

        then:"A 404 error is returned"
        flash.message == "default.not.found.message"
    }

    void "update action correctly persists"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * saveUpdate(_ as Project, _ as RemovedItems)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def project = new Project(params)
        project.id = 1

        controller.update(project, new RemovedItems())

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        def proj = new Project()
        proj.id = 1
        controller.projectService = Mock(ProjectService) {
            1 * saveUpdate(_ as Project, _ as RemovedItems) >> { Project project, RemovedItems removedItems ->
                throw new ValidationException("Invalid instance", project.errors)
            }
            1 * read(_) >> proj
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(proj, new RemovedItems())

        then:"The edit view is rendered again with the correct model"
        model.project instanceof Project
        params.projectId != null
        view == '/project/edit'
    }

    void "update action with constraint violation"() {
        given:
        Project proj = new Project(name: "test", code: "ccc").save()
        params.id = proj.id
        controller.projectService = Mock(ProjectService) {
            1 * saveUpdate(_ as Project, _ as RemovedItems) >> { Project project, RemovedItems removedItems ->
                throw new DataIntegrityViolationException("Foreign Key constraint violation")
            }
            1 * read(_) >> proj
        }

        when:"The update action is executed"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(proj, new RemovedItems())

        then:"The edit view is rendered again with the correct model"
        model.project instanceof Project
        controller.flash.error == "Removed entity has associated items and cannot be deleted"
        view == '/project/edit'
        params.projectId != null
    }

    void "delete action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.delete(1)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "PUT", "PATCH", "POST"]
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
        setToken(params)
        controller.delete(null)

        then:"A 404 is returned"
        flash.message == "default.not.found.message"
    }

    void "delete responds with 500 with invalid token"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null)

        then:"A 500 is returned"
        response.status == 500
    }

    void "Test the delete action with an instance"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        setToken(params)
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/projects'
        flash.message == "default.deleted.message"
    }

    void "delete action responds when data integrity violation exception thrown"() {
        given:
        def p = new Project()
        p.id = 2
        controller.projectService = Mock(ProjectService) {
            1 * delete(2) >> { Long id ->
                throw new DataIntegrityViolationException("Foreign Key constraint violation")
            }
            1 * read(_) >> p
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        setToken(params)
        controller.delete(2)

        then:"The edit view is rendered again with the correct model"
        model.project instanceof Project
        flash.error == "Project has associated items and cannot be deleted"
        view == 'show'
        params.projectId != null
    }

    void "projects action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.projects(1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH"]
    }

    void "projects action renders projects view"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when: "call index action"
        controller.projects()

        then: "view is returned"
        view == 'projects'
    }

    void "projects action returns the correct model"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.projects()

        then:"The model is correct"
        !model.projectList
        model.projectCount == 0
    }

    void "projects action param max"(Integer max, int expected) {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
            1 * count() >> 1
        }

        when:"the action is executed"
        controller.projects(max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
    }

    void "projects action sets sort and order when not included in params"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.projects()

        then:"The model is correct"
        controller.params.sort == 'name'
        controller.params.order == 'asc'
    }

    void "projects action retains sort and order when included in params"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        params.sort = 'testingSort'
        params.order = 'testingOrder'
        controller.projects()

        then:"The model is correct"
        controller.params.sort == 'testingSort'
        controller.params.order == 'testingOrder'
    }

    void "projects with search renders projects view"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * findAllByNameIlike('test', params) >> new SearchResult([new Project()], 1)
        }

        when: "call action"
        params.isSearch = 'true'
        params.searchTerm = 'test'
        setToken(params)
        controller.projects()

        then: "view is returned"
        view == '/project/projects'
    }

    void "projects action with search returns the correct model"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * findAllByNameIlike('test', params) >> new SearchResult([new Project()], 1)
        }

        when: "call action"
        params.isSearch = 'true'
        params.searchTerm = 'test'
        setToken(params)
        controller.projects()

        then:"The model is correct"
        model.projectList
        model.projectCount == 1
    }

    void "home action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.home(1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH", "POST"]
    }

    void "home action renders home view"() {
        given:
        def project = new Project()
        def sr = new SearchResult([], 0)
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> project
        }
        controller.bugService = Mock(BugService) {
            1 * countByProjectAndStatus(project, 'Open') >> 0
            1 * findAllByProject(project, _) >> sr
        }
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * countByProject(project) >> 0
        }
        controller.scenarioService = Mock(ScenarioService) {
            1 * countByProject(project) >> 0
        }
        controller.testCaseService = Mock(TestCaseService) {
            1 * countByProject(project) >> 0
        }
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * getPlansByStatus(project) >> new LinkedHashMap<>()
        }

        when:"a domain instance is passed to the home action"
        controller.home(2)

        then:
        view == "home"
    }

    void "home action with a null id"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(null) >> null
        }

        when:"The home action is executed with a null domain"
        controller.home(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "home action with a valid id"() {
        given:
        def project = new Project()
        def sr = new SearchResult([], 0)
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> project
        }
        controller.bugService = Mock(BugService) {
            1 * countByProjectAndStatus(project, 'Open') >> 0
            1 * findAllByProject(project, _) >> sr
        }
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * getPlansByStatus(project) >> [
                    "released": new ReleasePlan(plannedDate: futureDate, status: 'ToDo'),
                    "inProgress": new ReleasePlan(status: 'In Progress', plannedDate: futureDate)]
        }
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * countByProject(project) >> 0
        }
        controller.scenarioService = Mock(ScenarioService) {
            1 * countByProject(project) >> 0
        }
        controller.testCaseService = Mock(TestCaseService) {
            1 * countByProject(project) >> 0
        }

        when:"A domain instance is passed to the home action"
        controller.home(2)

        then:"A model is populated containing the domain instance"
        model.project instanceof Project
        model.bugCount == 0
        model.scenarioCount == 0
        model.testCaseCount == 0
        model.automatedTestCount == 0
        model.released != null
        model.current != null
        model.recentBugs == []
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

    void "show action with a null id"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action with a valid id"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> new Project()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.project instanceof Project
    }

    void "show action renders edit view"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> new Project()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }
}






