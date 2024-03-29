package com.everlution.test.ui.functional.specs.testcase.edit

import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.domains.TestCase
import com.everlution.services.testcase.TestCaseService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class EditPageSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
    }

    void "verify method and type field options"() {
        given:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Cases')

        browser.page(ListTestCasePage).listTable.clickCell("Name", 0)

        when:
        browser.page(ShowTestCasePage).goToEdit()

        then: "correct options populate for executionMethod and type"
        EditTestCasePage page = browser.page(EditTestCasePage)
        verifyAll {
            page.executionMethodOptions*.text() == ["", "Automated", "Manual"]
            page.typeOptions*.text() == ["", "API", "UI"]
        }
    }

    void "verify platform field options"() {
        given:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToProjectDomain('Test Cases')

        browser.page(ListTestCasePage).listTable.clickCell("Name", 0)

        when:
        browser.page(ShowTestCasePage).goToEdit()

        then: "correct options populate for executionMethod and type"
        EditTestCasePage page = browser.page(EditTestCasePage)
        verifyAll {
            page.platformOptions*.text() == ["", "Android", "iOS", "Web"]
        }
    }

    void "default steps panel displays for test with no steps"() {
        given: "create test case"
        Project project = projectService.list(max: 1).first()
        Person person = personService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        when: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        then: "edit the test case"
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.stepsTable.freeFormTab.displayed
        page.stepsTable.builderTab.displayed
    }
}
