package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.domains.Platform
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePageSpec extends GebSpec {

    ProjectService projectService

    void "platform options match project platforms"() {
        given: "get fake data"
        def pl = new Platform(name: "Testing 123")
        def pl1 = new Platform(name: "Testing 321")
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code, platforms: [pl, pl1]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', project.name)

        when: "go to the create bug page"
        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Bug")

        then: "correct options are populated"
        CreateBugPage page = browser.page(CreateBugPage)
        verifyAll {
            page.platformOptions*.text() == ["", "Testing 123", "Testing 321"]
        }

        and: "default value is blank"
        verifyAll( {
            page.platformSelect().selected == ""
        })
    }
}
