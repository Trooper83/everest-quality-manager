package com.manager.quality.everest.test.ui.functional.specs.bug.create

import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.project.ProjectHomePage
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.ui.support.pages.bug.CreateBugPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePageEnvironmentsSpec extends GebSpec {

    ProjectService projectService

    void "environments field has no value set"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        and: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        when: "project is selected"
        def page = browser.page(CreateBugPage)

        then: "default text selected"
        page.environmentsSelect().selectedText == []
        page.environmentsSelect().selected == []
        page.environmentsOptions[0].text() == "Select Environments..."
    }

    void "environment options equals project options"() {
        given:
        def env = new Environment(name: "Env is the only env")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code,
                environments: [env]))

        and:
        to LoginPage
        def loginPage = browser.page(LoginPage)

        when:
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def page = to CreateBugPage, project.id

        then:
        page.environmentsOptions*.text().containsAll("Env is the only env")
    }
}
