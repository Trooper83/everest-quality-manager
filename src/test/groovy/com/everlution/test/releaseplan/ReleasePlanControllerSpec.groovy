package com.everlution.test.releaseplan

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanController
import com.everlution.ReleasePlanService
import com.everlution.TestCycle
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.*

class ReleasePlanControllerSpec extends Specification implements ControllerUnitTest<ReleasePlanController>, DomainUnitTest<ReleasePlan> {

    def populateValidParams(params) {
        assert params != null

        params.name = "test release plan"
    }

    void "release plans action renders correct view"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * findAllByProject(_) >> []
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:
        controller.releasePlans(1)

        then:
        view == 'releasePlans'
    }

    void "release plans action returns the correct model"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * findAllByProject(_) >> [new ReleasePlan()]
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The index action is executed"
        controller.releasePlans(1)

        then:"The model is correct"
        model.releasePlanList
        model.releasePlanCount
        model.project
    }

    void "release plans action returns not found with invalid project"() {
        given:
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.releasePlans(null)

        then:
        response.status == 404
    }

    void "create action returns create view"() {
        given: "mock service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:
        controller.create()

        then:
        view == "create"
    }

    void "create action returns the correct model"() {
        given: "mock project service"
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The create action is executed"
        controller.create()

        then:"The model is correctly created"
        model.releasePlan instanceof ReleasePlan
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
        response.status == 404
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
        def project = new Project()
        project.id = 1
        releasePlan.project = project

        controller.save(releasePlan)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/releasePlan/show/1'
        controller.flash.message == "default.created.message"
    }

    void "save action with an invalid instance"() {
        given:
        def p = new Project()
        p.id = 1
        controller.projectService = Mock(ProjectService) {
            1 * read(_) >> p
        }
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * save(_ as ReleasePlan) >> { ReleasePlan releasePlan ->
                throw new ValidationException("Invalid instance", releasePlan.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        def releasePlan = new ReleasePlan()
        releasePlan.project = p
        controller.save(releasePlan)

        then:"The create view is rendered again with the correct model"
        model.releasePlan != null
        model.project == p
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
        controller.update(plan, null)

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
        controller.update(null, 1)

        then:"redirected to index"
        response.status == 404
    }

    void "update action with a valid plan instance and null projectId param returns 404"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(new ReleasePlan(), null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action with a projectId param not matching plan projectId returns 404"() {
        when:
        def releasePlan = new ReleasePlan(params)
        releasePlan.id = 1
        def project = new Project()
        project.id = 1
        releasePlan.project = project
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(releasePlan, 99)

        then:"A 404 error is returned"
        response.status == 404
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
        def project = new Project()
        project.id = 1
        releasePlan.project = project

        controller.update(releasePlan, 1)

        then:"A redirect is issued to the show action"
        response.redirectedUrl == '/project/1/releasePlan/show/1'
        controller.flash.message == "default.updated.message"
    }

    void "update action with an invalid instance"() {
        given:
        def releasePlan = new ReleasePlan(params)
        releasePlan.id = 1
        def project = new Project()
        project.id = 1
        releasePlan.project = project

        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * save(_ as ReleasePlan) >> { ReleasePlan plan ->
                throw new ValidationException("Invalid instance", plan.errors)
            }
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(releasePlan, 1)

        then:"The edit view is rendered again with the correct model"
        model.releasePlan != null
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

    void "delete action with a null instance returns 404"() {
        when:"The delete action is called for a null instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(null, 1)

        then:"redirect to index"
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

    void "delete action returns 404 with plan project not matching params project"() {
        given:
        def releasePlan = new ReleasePlan(params)
        releasePlan.id = 2
        def project = new Project()
        project.id = 1
        releasePlan.project = project

        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * read(2) >> releasePlan
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 999)

        then:"A 404 is returned"
        response.status == 404
    }

    void "delete action with an instance redirects to plans"() {
        given:
        def releasePlan = new ReleasePlan(params)
        releasePlan.id = 2
        def project = new Project()
        project.id = 1
        releasePlan.project = project

        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * delete(2)
            1 * read(2) >> releasePlan
        }

        when:"The domain instance is passed to the delete action"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'DELETE'
        controller.delete(2, 1)

        then:"The user is redirected to index"
        response.redirectedUrl == '/project/1/releasePlans'
        flash.message == "default.deleted.message"
    }

    void "add test cycle action returns 404 with a null release plan instance"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        controller.addTestCycle(null, new TestCycle())

        then:"404 error is returned"
        response.status == 404
    }

    void "add test cycle action returns 404 with a null test cycle instance"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        controller.addTestCycle(new ReleasePlan(), null)

        then:"404 error is returned"
        response.status == 404
    }

    void "add test cycle action correctly persists"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * addTestCycle(_ as ReleasePlan, _ as TestCycle) >> new ReleasePlan()
        }

        when:"save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        populateValidParams(params)
        def testCycle = new TestCycle(params)
        testCycle.id = 1
        def plan = new ReleasePlan()
        plan.id = 1
        def project = new Project()
        project.id = 1
        plan.project = project

        controller.addTestCycle(plan, testCycle)

        then:"redirect is issued to the show action"
        response.redirectedUrl == '/project/1/releasePlan/show/1'
        controller.flash.message != null
    }

    void "add test cycle action with an invalid instance"() {
        given:
        controller.releasePlanService = Mock(ReleasePlanService) {
            1 * addTestCycle(_ as ReleasePlan, _ as TestCycle) >> { ReleasePlan releasePlan, TestCycle testCycle ->
                throw new ValidationException("Invalid instance", releasePlan.errors)
            }
        }

        when:"add test cycles action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'POST'
        controller.addTestCycle(new ReleasePlan(), new TestCycle())

        then:"create view is rendered again with the correct model"
        model.testCycle != null
        view == 'create'
    }

    void "add test cycle action method type"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.addTestCycle(new ReleasePlan(), new TestCycle())

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'PUT'        | _
    }
}






