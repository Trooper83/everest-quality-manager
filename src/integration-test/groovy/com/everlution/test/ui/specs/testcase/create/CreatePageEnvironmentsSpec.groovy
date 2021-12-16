package com.everlution.test.ui.specs.testcase.create

import com.everlution.Environment
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreatePageEnvironmentsSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        setup: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        person = personService.list(max: 1).first()
    }

    void "environments field has no value set"() {
        given: "go to the create page"
        to CreateTestCasePage

        when: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "1"

        then: "default text selected"
        page.environmentsSelect().selectedText == []
        page.environmentsSelect().selected == []
    }

    void "environments field defaults with no environment option"() {
        given: "go to the create page"
        to CreateTestCasePage

        when: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "1"

        then: "default text present"
        page.environmentsOptions[0].text() == "--No Environment--"
    }

    void "environment field defaults disabled"() {
        given: "go to the create page"
        to CreateTestCasePage

        expect: "environment is disabled"
        def page = browser.page(CreateTestCasePage)
        page.environmentsSelect().disabled
    }

    void "environments field disabled and depopulated when project is set to default"() {
        given: "go to the create page"
        to CreateTestCasePage

        and: "project is selected"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = "1"

        expect: "environments field enabled and populated"
        waitFor() { //need to wait for transition
            !page.environmentsSelect().disabled
        }

        when: "project set to default"
        page.projectSelect().selected = ""

        then: "environments is disabled, depopulated and set to default"
        waitFor(2) { //need to wait for transition
            page.environmentsSelect().disabled
            page.environmentsOptions*.text() == ["--No Environment--"]
            page.environmentsSelect().selectedText == []
            page.environmentsSelect().selected == []
        }
    }

    void "environment select populates with only elements within the associated project"() {
        setup: "project & testCase instances with environments"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def tcd = DataFactory.testCase()
        def tc = new TestCase(person: person, name: tcd.name, executionMethod: tcd.executionMethod,
                type: tcd.type, project: project, environments: [env])
        testCaseService.save(tc)

        and: "go to the create page"
        to CreateTestCasePage

        when: "select project"
        def page = browser.page(CreateTestCasePage)
        page.projectSelect().selected = project.name

        then: "environment populates with project.environments"
        waitFor() {
            page.environmentsSelect().enabled
        }
        page.environmentsOptions*.text() == ["--No Environment--", env.name]
    }
}
