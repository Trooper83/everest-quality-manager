package com.everlution.test.ui.functional.specs.testiteration

import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestCycle
import com.everlution.TestCycleService
import com.everlution.TestGroup
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.DeniedPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.common.NotFoundPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ErrorsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService
    TestCaseService testCaseService
    TestCycleService testCycleService

    void "404 message displayed for not found"(String url) {
        given: "login as basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to show page for not found"
        go url

        then:
        NotFoundPage notFoundPage = at NotFoundPage
        notFoundPage.errors*.text().contains("Error: Page Not Found (404)")

        where:
        url << ["/project/1/testIteration/show/9999999999999999", "/project/1/testIteration/execute/9999999999999999"]
    }

    void "denied page displayed for read_only user"() {
        given: "setup data"
        def gd = DataFactory.testGroup()
        def group = new TestGroup(name: gd.name)
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, testGroups: [group])
        projectService.save(project)
        def plan = new ReleasePlan(name: "release plan 1", project: project, status: "ToDo")
        releasePlanService.save(plan)
        def testCycle = new TestCycle(name: "I am a test cycle", releasePlan: plan)
        releasePlanService.addTestCycle(plan, testCycle)
        def tc = DataFactory.testCase()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: tc.name, project: project, person: person, testGroups: [group])
        testCaseService.save(testCase)
        testCycleService.addTestIterations(testCycle, [testCase])

        and: "login as read_only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to cycle"
        go "/project/${project.id}/testCycle/show/${testCycle.id}"

        when:
        def iterationId = testCycle.testIterations.first().id
        go "/project/${project.id}/testIteration/execute/${iterationId}"

        then: "unauthorized message is displayed"
        DeniedPage page = at DeniedPage
        page.errors.text() == "Sorry, you're not authorized to view this page."
    }
}
