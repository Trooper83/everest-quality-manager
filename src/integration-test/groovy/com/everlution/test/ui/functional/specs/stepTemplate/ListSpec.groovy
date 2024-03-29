package com.everlution.test.ui.functional.specs.stepTemplate

import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.domains.StepTemplate
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.stepTemplate.ListStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ListSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/stepTemplates"

        then: "correct headers are displayed"
        def page = browser.page(ListStepTemplatePage)
        page.listTable.getHeaders() == ["Name", "Action", "Result", "Created By", "Created", "Updated"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        def page = to(ListStepTemplatePage, project.id)

        and:
        page.listTable.sortColumn(column)

        expect: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=asc')

        when:
        page.listTable.sortColumn(column)

        then: "correct params are displayed"
        currentUrl.contains("sort=${propName}")
        currentUrl.contains('order=desc')

        where:
        column | propName
        'Name' | 'name'
        'Created By' | 'person'
        'Created' | 'dateCreated'
        'Updated' | 'lastUpdated'
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/stepTemplates"

        when: "click first scenario in list"
        def listPage = browser.page(ListStepTemplatePage)
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowStepTemplatePage
    }

    void "search returns results and text retained in search box"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def projectPage = at ProjectHomePage
        projectPage.sideBar.goToProjectDomain('Step Templates')

        when:
        def page = at ListStepTemplatePage
        page.searchModule.search('step')

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'step'
    }

    void "reset button reloads results"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def projectPage = at ProjectHomePage
        projectPage.sideBar.goToProjectDomain('Step Templates')

        and:
        def page = at ListStepTemplatePage
        page.searchModule.search('step')

        expect:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'step'

        when:
        page.searchModule.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == ''
    }

    void "delete message displays after template deleted"() {
        given:
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def step = new StepTemplate(name: 'testing', person: person, project: project, act: 'action', result: 'result')
        def id = stepTemplateService.save(step).id

        and: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to list page"
        go "/project/${project.id}/stepTemplate/show/${id}"

        when:
        browser.page(ShowStepTemplatePage).delete()

        then: "at show page"
        def steps = at ListStepTemplatePage
        steps.statusMessage.displayed
    }
}
