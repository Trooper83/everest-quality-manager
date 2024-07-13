package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.domains.Platform
import com.everlution.domains.Project
import com.everlution.domains.TestCase
import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.services.testcase.TestCaseService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePagePlatformSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    void "platform has no value set"() {
        given:
        Project project = projectService.list(max:1).first()

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to CreateTestCasePage, project.id

        then:
        page.platformSelect().selectedText == ""
    }

    void "platform options equal project platforms"() {
        given:
        def p = DataFactory.area()
        def platform = new Platform(name: p.name)
        def pd = DataFactory.project()
        def person = personService.list(max: 1).first()
        Project proj = new Project(name: pd.name, code: pd.code, platforms: [platform])
        def project = projectService.save(proj)
        TestCase testCase = new TestCase(person: person,name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, platform: platform)
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to CreateTestCasePage, project.id

        then:
        page.platformOptions*.text() == ["", platform.name]
    }
}
