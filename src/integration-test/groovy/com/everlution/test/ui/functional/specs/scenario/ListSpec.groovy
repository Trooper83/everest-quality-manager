package com.everlution.test.ui.functional.specs.scenario

import com.everlution.domains.Area
import com.everlution.domains.Platform
import com.everlution.domains.Project
import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.domains.Scenario
import com.everlution.services.scenario.ScenarioService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.scenario.ShowScenarioPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class ListSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService

    void "verify list table headers order"() {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when: "go to list page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenarios"

        then: "correct headers are displayed"
        ListScenarioPage page = browser.page(ListScenarioPage)
        page.listTable.getHeaders() == ["Name", "Created By", "Area", "Platform", "Type", "Execution Method", "Created", "Updated"]
    }

    void "sort parameters correctly set in url"(String column, String propName) {
        given: "login as read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        def page = to(ListScenarioPage, project.id)

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
        'Area' | 'area'
        'Platform' | 'platform'
        'Type' | 'type'
        'Execution Method' | 'executionMethod'
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
        go "/project/${project.id}/scenarios"

        when: "click first scenario in list"
        def listPage = browser.page(ListScenarioPage)
        listPage.listTable.clickCell("Name", 0)

        then: "at show page"
        at ShowScenarioPage
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
        def projectPage = at ProjectHomePage
        projectPage.sideBar.goToProjectDomain('Scenarios')

        when:
        def page = at ListScenarioPage
        page.searchModule.search('scenario')

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'scenario'
    }

    void "delete message displays after scenario deleted"() {
        given: "scenario"
        def project = projectService.list(max: 1).first()
        def sd = DataFactory.scenario()
        def person = personService.list(max: 1).first()
        def scn = new Scenario(person: person, name: sd.name, executionMethod: "Manual", type: "UI",
                description: sd.description, project: project)
        def id = scenarioService.save(scn).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to scenario"
        go "project/${project.id}/scenario/show/${id}"

        when: "delete scenario"
        ShowScenarioPage showPage = browser.page(ShowScenarioPage)
        showPage.delete()

        then: "at list page and message displayed"
        def listPage = at ListScenarioPage
        listPage.statusMessage.text() ==~ /Scenario \d+ deleted/
    }

    void "reset button reloads results"() {
        given: "login as a read only user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and: "go to list page"
        def project = projectService.list(max: 1).first()
        def page = to(ListScenarioPage, project.id)
        page.searchModule.search('scenario')

        expect:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == 'scenario'

        when:
        page.searchModule.resetSearch()

        then:
        page.listTable.rowCount > 0
        page.searchModule.searchInput.text == ''
    }

    void "blank value when related items are null"() {
        given:
        def pd = DataFactory.project()
        def person = personService.list(max:1).first()
        def project = projectService.save(new Project(name: pd.name, code: pd.code))
        scenarioService.save(new Scenario(name: "related items testing scenario", project: project,
                person: person, executionMethod: "Automated", type: "UI"))

        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when:
        def page = to(ListScenarioPage, project.id)

        then:
        page.listTable.getValueInColumn(0, "Area") == ""
        page.listTable.getValueInColumn(0, "Platform") == ""
    }

    void "related items display name value"() {
        given:
        def area = new Area(name: "area list testing area")
        def platform = new Platform(name: "platform list testing platform")
        def pd = DataFactory.project()
        def person = personService.list(max:1).first()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area], platforms: [platform]))
        scenarioService.save(new Scenario(name: "related items testing scenario", project: project,
                person: person, executionMethod: "Automated", type: "UI", area: area, platform: platform))

        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to(ListScenarioPage, project.id)

        then:
        page.listTable.getValueInColumn(0, "Area") == "area list testing area"
        page.listTable.getValueInColumn(0, "Platform") == "platform list testing platform"
    }
}
