package com.manager.quality.everest.test.ui.functional.specs.testrun

import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.services.automatedtest.AutomatedTestService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.TestResult
import com.manager.quality.everest.domains.TestRun
import com.manager.quality.everest.services.testrun.TestRunService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.automatedtest.ShowAutomatedTestPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testrun.ShowTestRunPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ShowTestRunSpec extends GebSpec {

    AutomatedTestService automatedTestService
    ProjectService projectService
    TestRunService testRunService

    void "pass percentage has two decimals"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def proj = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "pass percent test", project: proj))
        def tr = new TestResult(result: "FAILED", automatedTest: a)
        def tr1 = new TestResult(result: "PASSED", automatedTest: a)
        def tr2 = new TestResult(result: "SKIPPED", automatedTest: a)
        def r = testRunService.save(new TestRun(name: "sorting test run show page", project: proj,
                testResults: [tr, tr1, tr2]))

        when:
        def page = to(ShowTestRunPage, proj.id, r.id)

        then:
        page.passPercentValue.text() == "33.33"
    }

    void "pass percentage 0.00 when no results"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def proj = projectService.list(max:1).first()
        def r = testRunService.save(new TestRun(name: "sorting test run show page", project: proj))

        when:
        def page = to(ShowTestRunPage, proj.id, r.id)

        then:
        page.passPercentValue.text() == "0.00"
    }

    void "link goes to automated test"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def proj = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "link goes to test", project: proj))
        def tr = new TestResult(result: "FAILED", automatedTest: a)
        def r = testRunService.save(new TestRun(name: "Linking test run", project: proj,
                testResults: [tr]))

        when:
        def page = to(ShowTestRunPage, proj.id, r.id)
        page.resultsTable.clickCell("Link To Test", 0)

        then:
        at ShowAutomatedTestPage
    }

    void "failure cause collapsed by default and expanded when clicked"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def proj = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "Failure cause collapsed", project: proj))
        def tr = new TestResult(result: "FAILED", automatedTest: a, failureCause: "I failed whoopsy")
        def r = testRunService.save(new TestRun(name: "Linking test run", project: proj,
                testResults: [tr]))

        and:
        def page = to(ShowTestRunPage, proj.id, r.id)

        expect:
        !page.failedCauseElement.first().displayed

        when:
        page.expandFailureCause()

        then:
        page.failedCauseElement.first().displayed
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def proj = projectService.list(max:1).first()
        def r = testRunService.save(new TestRun(name: "sorting test run show page", project: proj))
        def page = to(ShowTestRunPage, proj.id, r.id)

        and:
        page.resultsTable.sortColumn(column)

        expect: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=asc')

        when:
        page.resultsTable.sortColumn(column)

        then: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=desc')

        where:
        column           | propName
        'Automated Test' | 'automatedTest'
        'Result'         | 'result'
    }

    void "pagination works for results"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "test run test full name", project: p))
        def results = []
        int i = 0
        while(i < 27) {
            def r = new TestResult(result: "PASSED", automatedTest: a)
            results.add(r)
            i++
        }
        def t = testRunService.save(new TestRun(name: "pagination show page test", project: p, testResults: results))

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to(ShowTestRunPage, p.id, t.id)

        when:
        def c = page.resultsTable.getRowCount()
        page.scrollToBottom()
        page.scrollToBottom()
        page.resultsTable.goToPage('2')

        then:
        at ShowTestRunPage
        page.resultsTable.rowCount != c
    }

    void "test run stats are correct"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def proj = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "Test run stats are correct", project: proj))
        def tr = [
                new TestResult(result: "FAILED", automatedTest: a),
                new TestResult(result: "SKIPPED", automatedTest: a),
                new TestResult(result: "SKIPPED", automatedTest: a),
                new TestResult(result: "PASSED", automatedTest: a),
                new TestResult(result: "PASSED", automatedTest: a),
                new TestResult(result: "PASSED", automatedTest: a)
        ]
        def r = testRunService.save(new TestRun(name: "Test stats test run", project: proj,
                testResults: tr))

        when:
        def page = to(ShowTestRunPage, proj.id, r.id)

        then:
        page.totalValue.text() == "6"
        page.passPercentValue.text() == "50.00"
        page.passValue.text() == "3"
        page.failValue.text() == "1"
        page.skipValue.text() == "2"
    }

    void "test result row has correct result color"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def proj = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "Test row colors", project: proj))
        def tr = [
                new TestResult(result: "FAILED", automatedTest: a),
                new TestResult(result: "SKIPPED", automatedTest: a),
                new TestResult(result: "PASSED", automatedTest: a)
        ]
        def r = testRunService.save(new TestRun(name: "Test row colors test run", project: proj,
                testResults: tr))

        when:
        to(ShowTestRunPage, proj.id, r.id)

        then:
        $("span", text: "PASSED").hasClass("text-bg-success")
        $("span", text: "SKIPPED").hasClass("text-bg-warning")
        $("span", text: "FAILED").hasClass("text-bg-danger")
    }

    void "failureCause parsed with html breaks"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        def proj = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "Failure cause parsed with breaks", project: proj))
        def tr = new TestResult(result: "FAILED", automatedTest: a, failureCause: "This test failed \n with line breaks")
        def r = testRunService.save(new TestRun(name: "Failing test run", project: proj,
                testResults: [tr]))

        when:
        def page = to(ShowTestRunPage, proj.id, r.id)
        page.expandFailureCause()

        then:
        page.failedCauseElement.text() == 'This test failed\nwith line breaks'
    }
}
