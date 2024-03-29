package com.everlution.test.ui.functional.specs.automatedtest

import com.everlution.domains.AutomatedTest
import com.everlution.services.automatedtest.AutomatedTestService
import com.everlution.services.project.ProjectService
import com.everlution.domains.TestResult
import com.everlution.domains.TestRun
import com.everlution.services.testrun.TestRunService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.testrun.ShowTestRunPage
import com.everlution.test.ui.support.pages.automatedtest.ShowAutomatedTestPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ShowAutomatedTestSpec extends GebSpec {

    AutomatedTestService automatedTestService
    ProjectService projectService
    TestRunService testRunService

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

    }

    void "pass percentage has two decimals"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "pass percentage has two decimals", project: p))
        testRunService.save(
                new TestRun(project: p, name: "two decimals test run", testResults: [new TestResult(automatedTest: a, result: "PASSED")]))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)

        then:
        page.allTimePassPercentValue.text() == "100.00"
        page.recentPassPercentValue.text() == "100.00"
    }

    void "pass percentage zero when no results found"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "pass percentage has two decimals when no results", project: p))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)

        then:
        page.allTimePassPercentValue.text() == "0.00"
        page.recentPassPercentValue.text() == "0.00"
    }

    void "last 20 results display in table"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "should see 20 results", project: p))
        def results = []
        for (int i = 0; i <= 20; i++) {
            def r = new TestResult(automatedTest: a, result: "PASSED")
            results.add(r)
        }
        testRunService.save(
                new TestRun(project: p, name: "20 results test run", testResults: results))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)

        then:
        page.resultsTable.rowCount == 20
    }

    void "test run link redirects to show test run"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "redirect test run", project: p))
        testRunService.save(
                new TestRun(project: p, name: "redirect test run", testResults: [new TestResult(automatedTest: a, result: "PASSED")]))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)
        page.resultsTable.clickCell("Test Run", 0)

        then:
        at ShowTestRunPage
    }

    void "test statistics are correct"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "verify test stats", project: p))
        def results = [
                new TestResult(automatedTest: a, result: "PASSED"),
                new TestResult(automatedTest: a, result: "FAILED"),
                new TestResult(automatedTest: a, result: "FAILED"),
                new TestResult(automatedTest: a, result: "SKIPPED"),
                new TestResult(automatedTest: a, result: "SKIPPED"),
                new TestResult(automatedTest: a, result: "SKIPPED")
        ]
        testRunService.save(
                new TestRun(project: p, name: "verify test stats test run", testResults: results))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)

        then:
        page.allTimeTotalValue.text() == "6"
        page.allTimePassValue.text() == "1"
        page.allTimeFailValue.text() == "2"
        page.allTimeSkipValue.text() == "3"
        page.allTimePassPercentValue.text() == "16.67"
        page.recentTotalValue.text() == "6"
        page.recentPassValue.text() == "1"
        page.recentFailValue.text() == "2"
        page.recentSkipValue.text() == "3"
        page.recentPassPercentValue.text() == "16.67"
    }

    void "test result row has correct result color"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "verify test row colors", project: p))
        def results = [
                new TestResult(automatedTest: a, result: "PASSED"),
                new TestResult(automatedTest: a, result: "FAILED"),
                new TestResult(automatedTest: a, result: "SKIPPED")
        ]
        testRunService.save(
                new TestRun(project: p, name: "verify test row colors", testResults: results))

        when:
        to(ShowAutomatedTestPage, p.id, a.id)

        then:
        $("span", text: "PASSED").hasClass("text-bg-success")
        $("span", text: "SKIPPED").hasClass("text-bg-secondary")
        $("span", text: "FAILED").hasClass("text-bg-danger")
    }
}
