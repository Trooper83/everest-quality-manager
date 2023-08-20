package com.everlution.test.step

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.SearchResult
import com.everlution.Step
import com.everlution.StepController
import com.everlution.StepService
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import spock.lang.Specification

class StepControllerSpec extends Specification implements ControllerUnitTest<StepController> {

    def setup() {
    }

    def cleanup() {
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/stepController/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "steps action param max"(Integer max, int expected) {
        given:
        controller.stepService = Mock(StepService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the action is executed"
        controller.steps(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 10
        1    | 1
        99   | 99
        101  | 100
    }

    void "steps action renders steps view"() {
        given:
        controller.stepService = Mock(StepService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call action"
        controller.steps(1, 10)

        then: "view is returned"
        view == 'steps'
    }

    void "steps action returns the correct model"() {
        given:
        controller.stepService = Mock(StepService) {
            1 * findAllByProject(_, params) >> new SearchResult([new Step()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The action is executed"
        controller.steps(1, null)

        then:"The model is correct"
        model.stepList != null
        model.stepCount != null
        model.project != null
    }

    void "steps action returns not found with invalid project"() {
        given:
        controller.stepService = Mock(StepService) {
            0 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.steps(null, null)

        then:
        response.status == 404
    }

    void "steps search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.stepService = Mock(StepService) {
            1 * findAllInProjectByName(project, 'test', params) >> new SearchResult([new Step()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        setToken(params)
        params.isSearch = 'true'
        params.name = 'test'
        controller.steps(1, 10)

        then:"model is correct"
        model.stepList != null
        model.stepCount != null
        model.project != null
    }
}
