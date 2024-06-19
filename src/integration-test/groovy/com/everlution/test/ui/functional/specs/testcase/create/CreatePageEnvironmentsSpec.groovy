package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.domains.Environment
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
class CreatePageEnvironmentsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    void "environments field has no value set"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def project = projectService.list(max: 1).first()

        when: "go to the create page"
        go "/project/${project.id}/testCase/create"

        then: "default text selected"
        def page = browser.page(CreateTestCasePage)
        page.environmentsSelect().selectedText == []
        page.environmentsSelect().selected == []
    }

    void "environments equal project options"() {
        given:
        def e = DataFactory.area()
        def env = new Environment(name: e.name)
        def pd = DataFactory.project()
        def person = personService.list(max: 1).first()
        Project proj = new Project(name: pd.name, code: pd.code, environments: [env])
        def project = projectService.save(proj)
        TestCase testCase = new TestCase(person: person,name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project, environments: [env])
        testCaseService.save(testCase)

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to CreateTestCasePage, project.id

        then:
        page.environmentsOptions*.text() == ["Select Environments...", env.name]
    }
}
