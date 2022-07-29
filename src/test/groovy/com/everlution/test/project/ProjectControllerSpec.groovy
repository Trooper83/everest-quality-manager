package com.everlution.test.project

import com.everlution.BugService
import com.everlution.Project
import com.everlution.ProjectController
import com.everlution.ProjectService
import com.everlution.ScenarioService
import com.everlution.TestCaseService
import com.everlution.command.RemovedItems
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.*

class ProjectControllerSpec extends Specification implements ControllerUnitTest<ProjectController>, DomainUnitTest<Project> {

    def populateValidParams(params) {
        assert params != null

        params.name = "unit test project"
        params.code = "SOX"
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
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/project/index'
        flash.message == "default.not.found.message"
    }

    void "test the save action correctly persists"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * save(_ as Project)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def project = new Project(params)
        project.id = 1

        controller.save(project)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/home/1'
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
        def project = new Project()
        controller.save(project)

        then:"The create view is rendered again with the correct model"
        model.project != null
        view == 'create'
    }

    void "Test the edit action with a null id"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the edit action with a valid id"() {
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

    void "Test the update action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/project/index'
        flash.message == "default.not.found.message"
    }

    void "Test the update action correctly persists"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * saveUpdate(_ as Project, _ as RemovedItems)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        populateValidParams(params)
        def project = new Project(params)
        project.id = 1

        controller.update(project, new RemovedItems())

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/home/1'
        controller.flash.message == "default.updated.message"
    }

    void "Test the update action with an invalid instance"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * saveUpdate(_ as Project, _ as RemovedItems) >> { Project project, RemovedItems removedItems ->
                throw new ValidationException("Invalid instance", project.errors)
            }
            1 * read(_) >> {
                Mock(Project)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new Project(), new RemovedItems())

        then:"The edit view is rendered again with the correct model"
        model.project instanceof Project
        view == '/project/edit'
    }

    void "Test the update action with constraint violation"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * saveUpdate(_ as Project, _ as RemovedItems) >> { Project project, RemovedItems removedItems ->
                throw new DataIntegrityViolationException("Foreign Key constraint violation")
            }
            1 * read(_) >> {
                Mock(Project)
            }
        }

        Project project = new Project(name: "test", code: "ccc").save()
        params.id = project.id

        when:"The update action is executed"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'

        controller.update(project, new RemovedItems())

        then:"The edit view is rendered again with the correct model"
        model.project instanceof Project
        controller.flash.error == "Removed entity has associated items and cannot be deleted"
        view == '/project/edit'
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
        response.redirectedUrl == '/project/index'
        flash.message == "default.not.found.message"
    }

    void "Test the delete action with an instance"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * delete(2)
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The user is redirected to index"
        response.redirectedUrl == '/projects'
        flash.message == "default.deleted.message"
    }

    void "delete action responds when data integrity violation exception thrown"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * delete(2) >> { Long id ->
                throw new DataIntegrityViolationException("Foreign Key constraint violation")
            }
            1 * read(_) >> {
                new Project(params)
            }
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2)

        then:"The edit view is rendered again with the correct model"
        model.project instanceof Project
        flash.error == "Project has associated items and cannot be deleted"
        view == 'home'
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
        }

        when:"the action is executed"
        controller.projects(max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 10
        1    | 1
        99   | 99
        101  | 100
    }

    void "projects sets flash message when no projects found"() {
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
        }

        when:"the action is executed"
        controller.projects()

        then:
        flash.message == 'There are no projects in your organization'
    }

    void "projects with search sets flash message when no projects found"() {
        controller.projectService = Mock(ProjectService) {
            1 * findAllByNameIlike('test') >> []
        }

        when:"the action is executed"
        params.isSearch = 'true'
        params.name = 'test'
        controller.projects()

        then:
        flash.message == "No projects were found using search term: 'test'"
    }

    void "projects with search renders projects view"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * findAllByNameIlike('test') >> [new Project()]
        }

        when: "call action"
        params.isSearch = 'true'
        params.name = 'test'
        controller.projects()

        then: "view is returned"
        view == '/project/projects'
    }

    void "projects action with search returns the correct model"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * findAllByNameIlike('test') >> [new Project()]
        }

        when: "call action"
        params.isSearch = 'true'
        params.name = 'test'
        controller.projects()

        then:"The model is correct"
        model.projectList
        model.projectCount == 1
    }

    void "home action renders home view"() {
        given:
        def project = new Project()
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> project
        }
        controller.bugService = Mock(BugService) {
            1 * findAllByProject(project) >> []
        }
        controller.scenarioService = Mock(ScenarioService) {
            1 * findAllByProject(project) >> []
        }
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllByProject(project) >> []
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
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> project
        }
        controller.bugService = Mock(BugService) {
            1 * findAllByProject(project) >> []
        }
        controller.scenarioService = Mock(ScenarioService) {
            1 * findAllByProject(project) >> []
        }
        controller.testCaseService = Mock(TestCaseService) {
            1 * findAllByProject(project) >> []
        }

        when:"A domain instance is passed to the home action"
        controller.home(2)

        then:"A model is populated containing the domain instance"
        model.project instanceof Project
        model.bugCount == 0
        model.scenarioCount == 0
        model.testCaseCount == 0
    }
}






