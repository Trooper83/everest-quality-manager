package com.everlution.test.ui.specs.common

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.scenario.ListScenarioPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class NavBarSpec extends GebSpec {

    void "projects link forwards to projects view"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).navBar.goToListsPage("Projects")

        then:
        at ListProjectPage
    }

    void "bugs link forwards to project bugs view"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).navBar.goToListsPage("Bugs")

        then:
        at ListBugPage
    }

    void "release plan link forwards to project release plans view"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).navBar.goToListsPage("Release Plans")

        then:
        at ListReleasePlanPage
    }

    void "scenarios link forwards to project scenarios view"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).navBar.goToListsPage("Scenarios")

        then:
        at ListScenarioPage
    }

    void "test case link forwards to project test cases view"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).navBar.goToListsPage("Test Cases")

        then:
        at ListTestCasePage
    }

    void "test groups link forwards to project test groups view"() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).navBar.goToListsPage("Test Groups")

        then:
        at ListTestGroupPage
    }

    void "create and lists drop down not displayed on projects view"() {
        when: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        then:
        def projectsPage = at(ListProjectPage)
        !projectsPage.navBar.createMenuDropDown.displayed
        !projectsPage.navBar.listsMenuDropDown.displayed
    }

    void "create button hidden for read only users"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        then:
        !browser.page(ProjectHomePage).navBar.createMenuDropDown.displayed
    }

    void "create project button hidden for basic users"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        def page = browser.page(ProjectHomePage)
        page.navBar.createMenuDropDown.click()

        then:
        !page.navBar.isCreateMenuOptionDisplayed("Project")
    }

    void "create project button displayed for project admins and above"(String username) {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(username, "password")

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        def page = browser.page(ProjectHomePage)
        page.navBar.createMenuDropDown.click()

        then:
        page.navBar.isCreateMenuOptionDisplayed("Project")

        where:
        username << [Usernames.PROJECT_ADMIN.username, Usernames.APP_ADMIN.username, Usernames.ORG_ADMIN.username]
    }
}
