package com.manager.quality.everest.test.ui.functional.specs.testcase.edit

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.ui.support.pages.testcase.EditTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcase.ListTestCasePage
import com.manager.quality.everest.test.ui.support.pages.testcase.ShowTestCasePage
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
        def pl = new Platform(name: "Testing 123")
        def pl1 = new Platform(name: "Testing 321")
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code, platforms: [pl, pl1]))
        Person person = personService.list(max: 1).first()
        TestCase testCase = new TestCase(person: person, name: "first1", description: "desc1",
                executionMethod: "Automated", type: "API", project: project)
        def id = testCaseService.save(testCase).id

        when: "go to edit page"
        go "/project/${project.id}/testCase/edit/${id}"

        then: "correct options populate for executionMethod and type"
        EditTestCasePage page = browser.page(EditTestCasePage)
        verifyAll {
            page.platformOptions*.text().containsAll(project.platforms*.name)
            page.platformOptions.size() == 3
        }
    }
}
