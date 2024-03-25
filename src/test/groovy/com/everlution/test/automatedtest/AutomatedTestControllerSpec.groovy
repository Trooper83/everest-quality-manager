package com.everlution.test.automatedtest

import com.everlution.AutomatedTestResultsViewModel
import com.everlution.controllers.AutomatedTestController
import com.everlution.domains.AutomatedTest
import com.everlution.services.automatedtest.AutomatedTestService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.SearchResult
import com.everlution.services.testresult.TestResultService
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import spock.lang.Specification

class AutomatedTestControllerSpec extends Specification implements ControllerUnitTest<AutomatedTestController> {

    def setup() {
    }

    def cleanup() {
    }

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/automatedTest/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "automatedTests invalid methods"(String method) {
        when:
        request.method = method
        controller.automatedTests(null, null)

        then:
        status == 405

        where:
        method << ["PATCH", "DELETE", "PUT"]
    }

    void "automatedTests action param max"(Integer max, int expected) {
        given:
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the action is executed"
        controller.automatedTests(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
    }

    void "automatedTests action renders automatedTests view"() {
        given:
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call action"
        controller.automatedTests(1, 10)

        then: "view is returned"
        view == 'automatedTests'
    }


    void "automatedTests action returns the correct model"() {
        given:
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * findAllByProject(_, params) >> new SearchResult([new AutomatedTest()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The action is executed"
        controller.automatedTests(1, null)

        then:"The model is correct"
        model.automatedTestList != null
        model.automatedTestCount != null
        model.project != null
    }

    void "automatedTests action returns not found with invalid project"() {
        given:
        controller.automatedTestService = Mock(AutomatedTestService) {
            0 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.automatedTests(null, null)

        then:
        response.status == 404
    }

    void "automatedTests search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * findAllInProjectByFullName(project, 'test', params) >> new SearchResult([new AutomatedTest()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        setToken(params)
        request.method = 'POST'
        params.isSearch = 'true'
        params.searchTerm = 'test'
        controller.automatedTests(1, 10)

        then:"model is correct"
        model.automatedTestList != null
        model.automatedTestCount != null
        model.project != null
    }

    void "show invalid methods"(String method) {
        when:
        request.method = method
        controller.show(null)

        then:
        status == 405

        where:
        method << ["POST", "PATCH", "DELETE", "PUT"]
    }

    void "show action renders show view"() {
        given:
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * get(2) >> new AutomatedTest()
        }
        controller.testResultService = Mock(TestResultService) {
            1 * getResultsForAutomatedTest(_) >>
                    new AutomatedTestResultsViewModel(0,0,0,0,0,0,0,0,[])
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        view == "show"
    }

    void "show action returns correct model"() {
        given:
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * get(2) >> new AutomatedTest()
        }
        controller.testResultService = Mock(TestResultService) {
            1 * getResultsForAutomatedTest(_) >>
                    new AutomatedTestResultsViewModel(0,0,0,0,0,0,0,0,[])
        }

        when:"a domain instance is passed to the show action"
        controller.show(2)

        then:
        model.automatedTest != null
        model.resultModel != null
    }

    void "show action with a null id"() {
        given:
        controller.automatedTestService = Mock(AutomatedTestService) {
            1 * get(null) >> null
        }
        controller.testResultService = Mock(TestResultService) {
            1 * getResultsForAutomatedTest(_) >>
                    new AutomatedTestResultsViewModel(0,0,0,0,0,0,0,0,[])
        }

        when:"The show action is executed with a null domain"
        controller.show(null)

        then:"A 404 error is returned"
        response.status == 404
    }
}
