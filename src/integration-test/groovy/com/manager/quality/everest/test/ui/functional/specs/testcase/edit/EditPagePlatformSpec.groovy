package com.manager.quality.everest.test.ui.functional.specs.testcase.edit

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testcase.EditTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
@SendResults
class EditPagePlatformSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "platform select defaults with selected platform"() {
        setup: "project & test case instances with platforms"
        def platform = new Platform(name: "platform testing platform1234545")
        def project = projectService.save(new Project(name: "platform tc testing project I", code: "APP11", platforms: [platform]))
        def testCase = testCaseService.save(new TestCase(name: "platform testing test case I", project: project,
                person: person, executionMethod: "Automated", type: "UI", platform: platform))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        def page = to EditTestCasePage, project.id, testCase.id

        then: "testCase.platform is selected"
        page.platformSelect().selectedText == platform.name
    }

    void "platform select defaults empty text when no platform set"() {
        setup: "project & test case instances with platforms"
        def project = projectService.save(new Project(name: "platform tc testing project II", code: "APP22"))
        def testCase = testCaseService.save(new TestCase(name: "platform testing test case II", project: project,
                person: person, executionMethod: "Automated", type: "UI"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        def page = to EditTestCasePage, project.id, testCase.id

        then: "testCase.platform is selected"
        page.platformSelect().selectedText == ""
    }

    void "exception handled when validation error present and platform set to null"() {
        given: "project & test case instances with platforms"
        def platform = new Platform(name: "platform testing platform II")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def testCase = testCaseService.save(new TestCase(name: "platform testing test case II", project: project,
                person: person, executionMethod: "Automated", type: "UI", platform: platform))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/testCase/edit/${testCase.id}"

        and: "testCase.platform is set to null"
        def page = browser.page(EditTestCasePage)
        page.platformSelect().selected = ""

        expect: "platform set to null"
        page.platformSelect().selectedText == ""

        when: "add empty steps"
        page.scrollToBottom()
        page.stepsTable.addStep("", "", "")

        and: "submit"
        page.editTestCase()

        then: "at the edit page"
        at EditTestCasePage
    }

    void "options equal project platforms"() {
        given: "project & test case instances with platforms"
        def platform = new Platform(name: "platform testing platform13424")
        def project = projectService.save(new Project(name: "platform tc testing project I9789", code: "AP176", platforms: [platform]))
        def testCase = testCaseService.save(new TestCase(name: "platform testing test case I", project: project,
                person: person, executionMethod: "Automated", type: "UI", platform: platform))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditTestCasePage, project.id, testCase.id

        then:
        page.platformOptions*.text() == ["", platform.name]
    }
}
