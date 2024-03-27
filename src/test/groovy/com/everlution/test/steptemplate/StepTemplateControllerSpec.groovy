package com.everlution.test.steptemplate

import com.everlution.domains.Link
import com.everlution.services.link.LinkService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.RelatedStepTemplates
import com.everlution.SearchResult
import com.everlution.domains.StepTemplate
import com.everlution.controllers.StepTemplateController
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.controllers.command.LinksCmd
import com.everlution.controllers.command.RemovedItems
import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification

class StepTemplateControllerSpec extends Specification implements ControllerUnitTest<StepTemplateController> {

    def setup() {
    }

    def cleanup() {
    }

    def populateValidParams(params) {
        assert params != null

        params.name = 'this is the name'
        params.act = 'this is the act'
        params.result = 'this is the result'
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/stepTemplateController/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "stepTemplates action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.stepTemplates(1,1)

        then:
        response.status == 405

        where:
        httpMethod << ["DELETE", "PUT", "PATCH"]
    }

    void "stepTemplates action param max"(Integer max, int expected) {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * findAllInProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the action is executed"
        controller.stepTemplates(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
    }

    void "stepTemplates action renders steps view"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * findAllInProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call action"
        controller.stepTemplates(1, 10)

        then: "view is returned"
        view == 'stepTemplates'
    }

    void "stepTemplates action returns the correct model"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * findAllInProject(_, params) >> new SearchResult([new StepTemplate()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The action is executed"
        controller.stepTemplates(1, null)

        then:"The model is correct"
        model.stepTemplateList != null
        model.stepTemplateCount != null
        model.project != null
    }

    void "stepTemplates action returns not found with invalid project"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            0 * findAllInProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.stepTemplates(null, null)

        then:
        response.status == 404
    }

    void "stepTemplates search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * findAllInProjectByName(project, 'test', params) >> new SearchResult([new StepTemplate()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        setToken(params)
        params.isSearch = 'true'
        params.searchTerm = 'test'
        controller.stepTemplates(1, 10)

        then:"model is correct"
        model.stepTemplateList != null
        model.stepTemplateCount != null
        model.project != null
    }

    void "create action returns the correct view"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the create action is executed"
        controller.create(1)

        then:"the view is correctly created"
        view == "create"
    }

    void "create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The create action is executed"
        controller.create()

        then:"The model is correctly populated with template and projects"
        model.stepTemplate instanceof StepTemplate
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

    void "save action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def s = new StepTemplate(params)

        when:
        controller.save(s, new LinksCmd())

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
        controller.save(null, new LinksCmd())

        then:"A 404 error is returned"
        response.status == 404
    }

    void "save responds with 500 when no token present"() {
        when:"Save is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.save(null, new LinksCmd())

        then:"A 500 error is returned"
        response.status == 500
    }

    void "save action correctly persists"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate)
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
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project

        controller.save(t, new LinksCmd(links: []))

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/stepTemplate/show/1'
        controller.flash.message == "default.created.message"
    }

    void "save action with an invalid instance"() {
        given: "mock services"
        def p = new Project()
        p.id = 1
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate) >> { StepTemplate template ->
                throw new ValidationException("Invalid instance", template.errors)
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
        def t = new StepTemplate()
        t.project = p
        controller.save(t, new LinksCmd())

        then:"The create view is rendered again with the correct model"
        model.stepTemplate instanceof StepTemplate
        model.project == p
        view == 'create'
    }

    void "save sets flash error when validation fails for link"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate)
        }
        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }
        controller.linkService = Mock(LinkService) {
            1 * createSave(_) >> { Link link ->
                throw new ValidationException("Invalid instance", link.errors)
            }
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        setToken(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project

        controller.save(t, new LinksCmd(links: [new Link()]))

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/stepTemplate/show/1'
        controller.flash.message == "default.created.message"
        controller.flash.error == "An error occurred attempting to link templates"
    }

    void "save removes null links from list"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate)
        }
        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }
        controller.linkService = Mock(LinkService) {
            1 * createSave(_) >> new Link()
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        setToken(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project

        controller.save(t, new LinksCmd(links: [null, new Link()]))

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/stepTemplate/show/1'
        controller.flash.message == "default.created.message"
        !controller.flash.error
    }

    void "show action renders show view"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * get(2) >> new StepTemplate()
            1 * getLinkedTemplatesByRelation(_) >> [:]
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "show action with a null id"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * get(null) >> null
            1 * getLinkedTemplatesByRelation(_) >> [:]
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action with a valid id"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * get(2) >> new StepTemplate()
            1 * getLinkedTemplatesByRelation(_) >> [:]
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.stepTemplate instanceof StepTemplate
    }

    void "show action returns correct model"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * get(2) >> new StepTemplate()
            1 * getLinkedTemplatesByRelation(_) >> [:]
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance and relations"
        model.stepTemplate instanceof StepTemplate
        model.containsKey('relations')
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
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.edit(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "edit action with a valid id"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * get(2) >> new StepTemplate()
            1 * getLinkedTemplatesByRelation(_) >> [:]
        }

        when:"A domain instance is passed to the show action"
        controller.edit(2)

        then:"A model is populated containing the domain instance"
        model.stepTemplate instanceof StepTemplate
        model.linkedMap != null
    }

    void "edit action renders edit view"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * get(2) >> new StepTemplate()
        }

        when:"a domain instance is passed to the show action"
        controller.edit(2)

        then:
        view == "edit"
    }

    void "update action method"(String httpMethod) {
        given:
        request.method = httpMethod
        populateValidParams(params)
        def t = new StepTemplate(params)

        when:
        controller.update(t, null, null, null)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "POST", "PATCH"]
    }

    void "update action with a null step instance returns 404"() {
        when:"update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(null, 1, new LinksCmd(), new RemovedItems())

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update responds with 500 when no token present"() {
        when:"update is called for a domain instance that doesn't exist"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, 1, null, null)

        then:
        response.status == 500
    }

    void "update action with a valid step instance and null projectId param returns 404"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(new StepTemplate(), null, null, null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action with a projectId param not matching step projectId returns 404"() {
        when:
        def t = new StepTemplate()
        def project = new Project()
        project.id = 999
        t.project = project
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(t, 1, null, null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "test the update action correctly persists"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project

        controller.update(t, 1, new LinksCmd(), null)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/stepTemplate/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate) >> { StepTemplate template ->
                throw new ValidationException("Invalid instance", template.errors)
            }
            1 * read(_) >> {
                Mock(StepTemplate)
            }
        }

        when:"update action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project
        controller.update(t, 1, null, null)

        then:"The edit view is rendered again with the correct model"
        model.stepTemplate instanceof StepTemplate
        view == '/stepTemplate/edit'
    }

    void "update sets flash error when validation fails for link"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate) >> new StepTemplate()
        }
        controller.linkService = Mock(LinkService) {
            1 * createSave(_) >> { Link link ->
                throw new ValidationException("Invalid instance", link.errors)
            }
        }

        when:"update action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project
        controller.update(t, 1, new LinksCmd(links: [new Link()]), new RemovedItems())

        then:"The edit view is rendered again with the correct model"
        controller.flash.error == "An error occurred attempting to link templates"
    }

    void "update sets flash error when delete links fails"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate) >> new StepTemplate()
        }
        controller.linkService = Mock(LinkService) {
            1 * deleteRelatedLinks(_) >> {
                throw new Exception("Invalid instance")
            }
            1 * createSave(_) >> new Link()
        }

        when:"update action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project
        controller.update(t, 1, new LinksCmd(links: [new Link()]), new RemovedItems(linkIds: [1]))

        then:"The edit view is rendered again with the correct model"
        controller.flash.error == "An error occurred attempting to delete links"
    }

    void "update action correctly removes null links"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate)
        }
        controller.linkService = Mock(LinkService) {
            1 * deleteRelatedLinks(_)
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project

        controller.update(t, 1, new LinksCmd(links: [null]), new RemovedItems())

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/stepTemplate/show/1'
        controller.flash.message == "default.updated.message"
        !controller.flash.error
    }

    void "update repopulates links after validation failure"() {
        given:
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * save(_ as StepTemplate) >> { StepTemplate template ->
                throw new ValidationException("Invalid instance", template.errors)
            }
            1 * read(_) >> {
                Mock(StepTemplate)
            }
            1 * getLinkedTemplatesByRelation(_) >> [:]
        }

        when:"update action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 1
        project.id = 1
        t.project = project
        controller.update(t, 1, null, null)

        then:"The edit view is rendered again with the correct model"
        model.linkedMap instanceof Map
    }

    void "update action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.update(null, null, null, null)

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PATCH", "POST"]
    }

    void "delete action returns 405 with not allowed method types"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.delete(1,1)

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

    void "delete action returns 404 with step project not matching params project"() {
        given:
        params.projectId = 999
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 2
        project.id = 1
        t.project = project

        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * read(2) >> t
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
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 2
        project.id = 1
        t.project = project

        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * delete(2)
            1 * read(2) >> t
        }

        when:"The domain instance is passed to the delete action"
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"The user is redirected to index"
        response.redirectedUrl == '/project/1/stepTemplates'
        flash.message == "default.deleted.message"
    }

    void "delete redirects when exception thrown"() {
        given:
        params.projectId = 1
        populateValidParams(params)
        def t = new StepTemplate(params)
        def project = new Project()
        t.id = 2
        project.id = 1
        t.project = project

        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * delete(2) >> {
                throw new DataIntegrityViolationException("Invalid instance", new Throwable("message"))
            }
            1 * read(2) >> t
        }

        when:"The domain instance is passed to the delete action"
        setToken(params)
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"The user is redirected to index"
        response.redirectedUrl == '/project/1/stepTemplate/show/2'
        flash.error == "An issue occurred attempting to delete the template"
    }

    void "search returns 404 when project is null"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> null
        }

        when:
        controller.search(null, "")

        then:
        response.status == 404
    }

    void "search returns 405 for not allowed methods"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.search(1, "")

        then:
        response.status == 405

        where:
        httpMethod << ["PUT", "POST", "PATCH", "DELETE"]
    }

    void "getRelatedTemplates 405 for not allowed methods"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.getRelatedTemplates(1, 1)

        then:
        response.status == 405

        where:
        httpMethod << ["PUT", "POST", "PATCH", "DELETE"]
    }

    void "getRelatedTemplates returns 404 when project null"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> null
        }

        when:
        controller.getRelatedTemplates(1, 1)

        then:
        response.status == 404
    }

    void "getRelatedTemplates returns 404 when id null"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> new Project()
        }

        when:
        controller.getRelatedTemplates(1, null)

        then:
        response.status == 404
    }

    void "getRelatedTemplates renders validation error when step not found"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> new Project()
        }
        controller.stepTemplateService = Mock(StepTemplateService) {
            1 * getRelatedTemplates(_) >> new RelatedStepTemplates(null, [])
        }

        when:
        controller.getRelatedTemplates(1, 111111)

        then:
        response.status == 404
    }
}
