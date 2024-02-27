package com.everlution.test.ui.functional.specs.automatedtest

import com.everlution.AutomatedTest
import com.everlution.AutomatedTestService
import com.everlution.ProjectService
import com.everlution.TestResult
import com.everlution.TestRun
import com.everlution.TestRunService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.testrun.ShowTestRunPage
import com.everlution.test.ui.support.pages.automatedtest.ShowAutomatedTestPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

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
                new TestRun(project: p, name: "two decimals test run", testResults: [new TestResult(automatedTest: a, result: "Passed")]))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)

        then:
        page.allTimePassValue.text() == "100.00"
        page.recentPassValue.text() == "100.00"
    }

    void "pass percentage zero when no results found"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "pass percentage has two decimals when no results", project: p))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)

        then:
        page.allTimePassValue.text() == "0.00"
        page.recentPassValue.text() == "0.00"
    }

    void "last 20 results display in table"() {
        given:
        def p = projectService.list(max:1).first()
        def a = automatedTestService.save(new AutomatedTest(fullName: "should see 20 results", project: p))
        def results = []
        for (int i = 0; i <= 20; i++) {
            def r = new TestResult(automatedTest: a, result: "Passed")
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
                new TestRun(project: p, name: "redirect test run", testResults: [new TestResult(automatedTest: a, result: "Passed")]))

        when:
        def page = to(ShowAutomatedTestPage, p.id, a.id)
        page.resultsTable.clickCell("Test Run", 0)

        then:
        at ShowTestRunPage
    }
}
