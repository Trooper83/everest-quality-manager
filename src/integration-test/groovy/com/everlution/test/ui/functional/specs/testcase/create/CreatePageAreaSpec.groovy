package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.domains.Area
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
class CreatePageAreaSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    void "area field has no value set"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def project = projectService.list(max: 1).first()

        when: "go to the create page"
        go "/project/${project.id}/testCase/create"

        then: "default text selected"
        def page = browser.page(CreateTestCasePage)
        page.areaSelect().selectedText == ""
        page.areaSelect().selected == ""
    }

    void "area options equal project options"() {
        given:
        def a = DataFactory.area()
        def area = new Area(name: a.name)
        def pd = DataFactory.project()
        def person = personService.list(max: 1).first()
        Project proj = new Project(name: pd.name, code: pd.code, areas: [area])
        def project = projectService.save(proj)
        TestCase testCase = new TestCase(person: person,name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, area: area)
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to CreateTestCasePage, project.id

        then:
        page.areaOptions*.text() == ["", area.name]
    }
}
