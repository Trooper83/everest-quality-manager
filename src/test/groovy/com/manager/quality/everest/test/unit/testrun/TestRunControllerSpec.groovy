package com.manager.quality.everest.test.unit.testrun

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.RunWithPaginatedResults
import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.domains.TestRun
import com.manager.quality.everest.controllers.TestRunController
import com.manager.quality.everest.services.testrun.TestRunService
import com.manager.quality.everest.controllers.command.TestRunCmd
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import org.grails.web.servlet.mvc.SynchronizerTokensHolder
import spock.lang.Specification

class TestRunControllerSpec extends Specification implements ControllerUnitTest<TestRunController>, DomainUnitTest<TestRun> {

    def setToken(params) {
        def token = SynchronizerTokensHolder.store(session)
        params[SynchronizerTokensHolder.TOKEN_URI] = '/testRun/action'
        params[SynchronizerTokensHolder.TOKEN_KEY] = token.generateToken(params[SynchronizerTokensHolder.TOKEN_URI])
    }

    void "testRuns invalid methods"(String method) {
        when:
        request.method = method
        controller.testRuns(null, null)

        then:
        status == 405

        where:
        method << ["PATCH", "DELETE", "PUT"]
    }

    void "testRuns action param max"(Integer max, int expected) {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"the action is executed"
        controller.testRuns(1, max)

        then:"the max is as expected"
        controller.params.max == expected

        where:
        max  | expected
        null | 25
        1    | 1
        99   | 99
        101  | 100
    }

    void "testRuns action renders testRuns view"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call action"
        controller.testRuns(1, 10)

        then: "view is returned"
        view == 'testRuns'
    }

    void "testRuns action sets order and sort params when not found"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call action"
        controller.testRuns(1, 10)

        then:
        controller.params.sort == 'dateCreated'
        controller.params.order == 'desc'
    }

    void "testRuns action retains sort and order params"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when: "call action"
        params.sort = 'testSort'
        params.order = 'testOrder'
        controller.testRuns(1, 10)

        then:
        controller.params.sort == 'testSort'
        controller.params.order == 'testOrder'
    }

    void "testRuns action returns the correct model"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * findAllByProject(_, params) >> new SearchResult([new TestRun()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> new Project()
        }

        when:"The action is executed"
        controller.testRuns(1, null)

        then:"The model is correct"
        model.testRunList != null
        model.testRunCount != null
        model.project != null
    }

    void "testRuns action returns not found with invalid project"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            0 * findAllByProject(_, params) >> new SearchResult([], 0)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> null
        }

        when:"The action is executed"
        controller.testRuns(null, null)

        then:
        response.status == 404
    }

    void "testRuns search returns the correct model"() {
        def project = new Project(name: 'test')
        controller.testRunService = Mock(TestRunService) {
            1 * findAllInProjectByName(project, 'test', params) >> new SearchResult([new TestRun()], 1)
        }
        controller.projectService = Mock(ProjectService) {
            1 * get(_) >> project
        }

        when:"action is executed"
        setToken(params)
        request.method = 'POST'
        params.isSearch = 'true'
        params.searchTerm = 'test'
        controller.testRuns(1, 10)

        then:"model is correct"
        model.testRunList != null
        model.testRunCount != null
        model.project != null
    }

    void "show invalid methods"(String method) {
        when:
        request.method = method
        controller.show(null, null)

        then:
        status == 405

        where:
        method << ["POST", "PATCH", "DELETE", "PUT"]
    }

    void "show action renders show view"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * getWithPaginatedResults(_, _) >> new RunWithPaginatedResults(new TestRun(), [])
        }

        when:"a domain instance is passed to the show action"
        controller.show(2, null)

        then:
        view == "show"
    }

    void "show action returns correct model"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * getWithPaginatedResults(_, _) >> new RunWithPaginatedResults(new TestRun(), [])
        }

        when:"a domain instance is passed to the show action"
        controller.show(2, null)

        then:
        model.testRun != null
        model.results != null
    }

    void "show action with a null id"() {
        given:
        controller.testRunService = Mock(TestRunService) {
            1 * getWithPaginatedResults(_, _) >> new RunWithPaginatedResults(null, [])
        }

        when:"The show action is executed with a null domain"
        controller.show(null, null)

        then:"A 404 error is returned"
        response.status == 404
    }

    void "save action methods not allowed returns 405"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.save(new TestRunCmd())

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PUT", "PATCH"]
    }

    void "save returns bad request when project not found"() {
        given:
        def cmd = new TestRunCmd(name: "name", project: null)

        when:
        request.method = "POST"
        controller.save(cmd)

        then:
        status == 400
        model.isEmpty()
        response.format == 'json'
    }

    void "save returns bad request when name is invalid"(String name) {
        given:
        def p = new Project(name: "name 123", code: "987").save()
        def cmd = new TestRunCmd(name: name, project: p)

        when:
        request.method = "POST"
        controller.save(cmd)

        then:
        status == 400

        where:
        name << [null, ""]
    }

    void "save returns 201 when null results provided"() {
        given:
        def p = new Project(name: "name 123", code: "987").save()
        def cmd = new TestRunCmd(name: "Name", project: p, testResults: null)
        controller.testRunService = Mock(TestRunService) {
            1 * createAndSave(_,_,_) >> new TestRun()
        }

        when:
        request.method = "POST"
        controller.save(cmd)

        then:
        status == 201
    }

    void "save 201 returned when empty results provided"() {
        given:
        def p = new Project(name: "name 123", code: "987").save()
        def cmd = new TestRunCmd(name: "Name", project: p, testResults: [])
        def t = new TestRun(name: "testing", project: p).save()
        controller.testRunService = Mock(TestRunService) {
            1 * createAndSave(_,_,_) >> t
        }

        when:
        request.method = "POST"
        controller.save(cmd)

        then:
        status == 201
        response.text == "TestRun ${t.id} created"
    }
}
