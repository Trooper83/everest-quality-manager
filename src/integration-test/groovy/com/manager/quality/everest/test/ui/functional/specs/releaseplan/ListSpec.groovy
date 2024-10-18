package com.manager.quality.everest.test.ui.functional.specs.releaseplan

import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ListSpec extends GebSpec {

    PersonService personService
    ProjectService projectService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when: "go to list page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)

        then: "correct headers are displayed"
        page.listTable.getHeaders() == ["Name", "Status", "Created By", "Planned", "Released", "Created", "Updated"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projId = projectService.list(max:1).first().id
        def page = to(ListReleasePlanPage, projId)

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
        'Status' | 'status'
        'Created By' | 'person'
        'Planned' | 'plannedDate'
        'Released' | 'releaseDate'
        'Created' | 'dateCreated'
        'Updated' | 'lastUpdated'
    }

    void "clicking name column directs to show page"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)

        when: "click first in list"
        page.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowReleasePlanPage
    }

    void "search returns results"() {
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list project page"
        def listPage = to ListProjectPage
        listPage.projectTable.clickCell('Name', 0)

        and:
        def page = at ProjectHomePage
        page.sideBar.goToProjectDomain('Release Plans')

        when:
        def plansPage = at ListReleasePlanPage
        plansPage.searchModule.search('plan')

        then:
        plansPage.listTable.rowCount > 0
        plansPage.searchModule.searchInput.text == 'plan'
    }

    void "delete message displays after plan deleted"() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def plan = DataFactory.createReleasePlan(person, project)

        and: "go to show page"
        def showPage = to(ShowReleasePlanPage, project.id, plan.id)

        when: "delete"
        showPage.deletePlan()

        then: "at list page and message displayed"
        def listPage = at ListReleasePlanPage
        listPage.statusMessage.text() ==~ /ReleasePlan \d+ deleted/
    }

    void "reset button reloads results"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def id = projectService.list(max: 1).first().id
        def page = to(ListReleasePlanPage, id)

        and:
        page.searchModule.search('plan')

        expect:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'plan'

        when:
        page.searchModule.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == ''
    }
}
