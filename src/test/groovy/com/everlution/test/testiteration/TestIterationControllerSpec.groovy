package com.everlution.test.testiteration

import com.everlution.Person
import com.everlution.Project
import com.everlution.ReleasePlan
import com.everlution.TestCycle
import com.everlution.TestIteration
import com.everlution.TestIterationController
import com.everlution.TestIterationService
import com.everlution.TestResult

import grails.plugin.springsecurity.SpringSecurityService
import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import spock.lang.Specification

class TestIterationControllerSpec extends Specification implements ControllerUnitTest<TestIterationController>, DataTest {

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/testCase/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "show action renders show view"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "show action with null id returns 404"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(null) >> null
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "show action with a valid id returns a test iteration"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"A domain instance is passed to the show action"
        controller.show(2)

        then:"A model is populated containing the domain instance"
        model.testIteration instanceof TestIteration
    }

    void "execute action renders execute view"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"a domain instance is passed to the execute action"
        controller.execute(2)

        then:
        view == "execute"
    }

    void "execute action with null id returns 404"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(null) >> null
        }

        when:"The execute action is executed with a null domain"
        controller.execute(null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "execute action with a valid id returns a test iteration"() {
        given:
        controller.testIterationService = Mock(TestIterationService) {
            1 * get(2) >> new TestIteration()
        }

        when:"A domain instance is passed to the execute action"
        controller.execute(2)

        then:"A model is populated containing the domain instance"
        model.testIteration instanceof TestIteration
    }

    void "update returns 404 with null id"() {
        when:
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(null, 1)

        then:
        response.status == 404
    }

    void "update responds with 500 when no token present"() {
        when:
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        controller.update(null, 1)

        then:
        response.status == 500
    }

    void "update action with a valid iteration instance and null projectId param returns 404"() {
        when:
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        controller.update(new TestIteration(), null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action with a projectId param not matching iteration projectId returns 404"() {
        when:
        mockDomain(TestCycle)
        def iteration = new TestIteration()
        iteration.id = 1
        def plan = new ReleasePlan()
        def project = new Project()
        project.id = 1
        plan.project = project
        def cycle = new TestCycle()
        cycle.releasePlan = plan
        iteration.testCycle = cycle
        request.contentType = FORM_CONTENT_TYPE
        setToken(params)
        request.method = 'PUT'
        controller.update(iteration, 9999)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "update action invalid method returns 405"(String httpMethod) {
        given:
        request.method = httpMethod
        def iteration = new TestIteration()

        when:
        controller.update(iteration, null)

        then:
        response.status == 405

        where:
        httpMethod   | _
        'GET'        | _
        'DELETE'     | _
        'POST'       | _
    }

    void "update action correctly persists"() {
        given:
        mockDomain(TestCycle)
        controller.testIterationService = Mock(TestIterationService) {
            1 * save(_ as TestIteration)
        }

        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }

        when:"The save action is executed with a valid instance"
        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        def iteration = new TestIteration()
        iteration.id = 1
        def plan = new ReleasePlan()
        def project = new Project()
        project.id = 1
        plan.project = project
        def cycle = new TestCycle()
        cycle.releasePlan = plan
        iteration.testCycle = cycle

        controller.update(iteration, 1)

        then:"A redirect is issued to the show action"
        controller.flash.message == "default.updated.message"
    }

    void "update action correctly sets person and dateExecuted"() {
        given:
        mockDomain(TestCycle)
        controller.testIterationService = Mock(TestIterationService) {
            1 * save(_ as TestIteration)
        }

        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser() >> new Person()
        }

        response.reset()
        request.contentType = FORM_CONTENT_TYPE
        request.method = 'PUT'
        setToken(params)
        def iteration = new TestIteration()
        iteration.id = 1
        def plan = new ReleasePlan()
        def project = new Project()
        project.id = 1
        plan.project = project
        def cycle = new TestCycle()
        cycle.releasePlan = plan
        iteration.testCycle = cycle

        expect:
        iteration.dateExecuted == null
        iteration.person == null

        when:"The save action is executed with a valid instance"
        controller.update(iteration, 1)

        then:
        iteration.dateExecuted != null
        iteration.person != null
    }

    void "update action with an invalid instance"() {
        given:
        def iteration = new TestIteration()
        iteration.id = 1
        def plan = new ReleasePlan()
        def project = new Project()
        project.id = 1
        plan.project = project
        def cycle = new TestCycle()
        cycle.releasePlan = plan
        iteration.testCycle = cycle

        controller.testIterationService = Mock(TestIterationService) {
            1 * save(_ as TestIteration) >> { TestIteration testIteration ->
                throw new ValidationException("Invalid instance", testIteration.errors)
            }
            1 * read(_) >> iteration
        }

        controller.springSecurityService = Mock(SpringSecurityService) {
            1 * getCurrentUser()
        }

        when:"The save action is executed with an invalid instance"
        request.contentType = FORM_CONTENT_TYPE
        setToken(params)
        request.method = 'PUT'
        controller.update(iteration, 1)

        then:"The edit view is rendered again with the correct model"
        model.testIteration != null
        view == '/testIteration/execute'
    }
}
