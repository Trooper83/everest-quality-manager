package com.everlution.test.project

import com.everlution.Area
import com.everlution.Environment
import com.everlution.Project
import com.everlution.ProjectController
import com.everlution.ProjectService
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

    void "test index action renders index view"() {
        given:
        controller.projectService = Mock(ProjectService) {
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
        controller.projectService = Mock(ProjectService) {
            1 * list(_) >> []
            1 * count() >> 0
        }

        when:"The index action is executed"
        controller.index()

        then:"The model is correct"
        !model.projectList
        model.projectCount == 0
    }

    void "test the index action param max"(Integer max, int expected) {
        given:
        controller.projectService = Mock(ProjectService) {
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
        when:"the create action is executed"
        controller.create()

        then:"the model is correctly created"
        view == "create"
    }

    void "Test the create action returns the correct model"() {
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

    void "Test the save action with a null instance"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null)

        then:"A 404 error is returned"
        response.redirectedUrl == '/project/index'
        flash.message == "default.not.found.message"
    }

    void "Test the save action correctly persists"() {
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
        response.redirectedUrl == '/project/show/1'
        controller.flash.message == "default.created.message"
    }

    void "Test the save action with an invalid instance"() {
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

    void "test the show action renders show view"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> new Project()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "Test the show action with a null id"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "Test the show action with a valid id"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(2) >> new Project()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.project instanceof Project
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
        response.redirectedUrl == '/project/show/1'
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
        response.redirectedUrl == '/project/index'
        flash.message == "default.deleted.message"
    }

    void "Test the getProjectItems action with a null instance"() {
        when:"The delete action is called for a null instance"
        request.method = 'GET'
        controller.getProjectItems(null)

        then:"A 404 is returned"
        response.status == 404
    }

    void "Test the getProjectItems action with an instance"() {
        given: "a valid project"
        params.name = "unit test project"
        params.code = "SOX"
        params.areas = [new Area(name:"testing")]
        def project = new Project(params)

        when:"The domain instance is passed to the getAreas action"
        request.method = 'GET'
        controller.getProjectItems(project)

        then:"areas are returned"
        response.status == 200
    }

    void "test the getProjectItems action method"(String httpMethod) {
        when:"The domain instance is passed to the getProjectItems action"
        request.method = httpMethod
        controller.getProjectItems(null)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "POST", "PATCH"]
    }

    void "getProjectItems returns areas and environments from project"() {
        given: "project with area and environments"
        def areaList = [new Area(name: "area 51")]
        def envList = [new Environment(name: "environment 51")]
        def project = new Project(name: "testing project", code: "tpc", areas: areaList,
                environments: envList).save()

        when: "call action"
        controller.getProjectItems(project)

        then: "items returned"
        model.areas == areaList
        model.environments == envList
    }
}





