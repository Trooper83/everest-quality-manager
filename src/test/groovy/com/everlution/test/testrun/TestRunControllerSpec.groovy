package com.everlution.test.testrun

import com.everlution.Project
import com.everlution.TestResult
import com.everlution.TestResultService
import com.everlution.TestRun
import com.everlution.TestRunController
import com.everlution.TestRunService
import com.everlution.command.TestRunCmd
import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class TestRunControllerSpec extends Specification implements ControllerUnitTest<TestRunController>, DomainUnitTest<TestRun> {


    void "methods not allowed returns 405"(String httpMethod) {
        given:
        request.method = httpMethod

        when:
        controller.save(new TestRunCmd())

        then:
        response.status == 405

        where:
        httpMethod << ["GET", "DELETE", "PUT", "PATCH"]
    }

    void "returns bad request when project not found"() {
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

    void "returns bad request when name is invalid"(String name) {
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

    void "201 when null results provided"() {
        given:
        def p = new Project(name: "name 123", code: "987").save()
        def cmd = new TestRunCmd(name: "Name", project: p, testResults: null)
        controller.testResultService = Mock(TestResultService) {
            1 * createAndSave(_,_) >> []
        }
        controller.testRunService = Mock(TestRunService) {
            1 * save(_) >> new TestRun(name: "name", project: p)
        }

        when:
        request.method = "POST"
        controller.save(cmd)

        then:
        status == 201
    }

    void "201 returned when empty results provided"() {
        given:
        def p = new Project(name: "name 123", code: "987").save()
        def cmd = new TestRunCmd(name: "Name", project: p, testResults: [])
        def t = new TestRun(name: "testing", project: p).save()
        controller.testResultService = Mock(TestResultService) {
            1 * createAndSave(_,_) >> []
        }
        controller.testRunService = Mock(TestRunService) {
            1 * save(_) >> t
        }

        when:
        request.method = "POST"
        controller.save(cmd)

        then:
        status == 201
        response.text == "TestRun ${t.id} created"
    }

    void "bad request when test result invalid"() {
        given:
        controller.testResultService = Mock(TestResultService) {
            1 * createAndSave(_,_) >> {
                def tr = new TestResult()
                throw new ValidationException("Invalid", tr.errors)
            }
        }
        def p = new Project(name: "name 123", code: "987").save()
        def cmd = new TestRunCmd(name: "Name", project: p, testResults: null)

        when:
        request.method = "POST"
        controller.save(cmd)

        then:
        status == 400
        model.isEmpty()
        response.format == 'json'
    }
}
