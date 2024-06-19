package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.domains.Platform
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePagePlatformSpec extends GebSpec {

    ProjectService projectService

    void "platform field has no value set"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        then: "default text selected"
        def page = browser.page(CreateBugPage)
        page.platformSelect().selectedText == ""
        page.platformSelect().selected == ""
    }

    void "platform options equal project"() {
        given:
        def platform = new Platform(name: "Platform is the only platform")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code,
                platforms: [platform]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to CreateBugPage, project.id

        then:
        page.platformOptions*.text().containsAll("Platform is the only platform")
    }
}
