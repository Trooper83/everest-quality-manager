package com.everlution.test.ui.specs.common

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.person.ProfilePage
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

    void "project link displayed on pages without project param"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).navBar.goToProfile()

        expect:
        def page = at ProfilePage

        when:
        page.navBar.goToProjects()

        then:
        at ListProjectPage
    }

    void "projects dropdown bugs redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).navBar.goToProjectDomain("Bugs")

        then:
        at ListBugPage
    }

    void "projects dropdown release plan redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).navBar.goToProjectDomain("Release Plans")

        then:
        at ListReleasePlanPage
    }

    void "projects dropdown scenario redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).navBar.goToProjectDomain("Scenarios")

        then:
        at ListScenarioPage
    }

    void "projects dropdown test case redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).navBar.goToProjectDomain("Test Cases")

        then:
        at ListTestCasePage
    }

    void "projects dropdown test group redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).navBar.goToProjectDomain("Test Groups")

        then:
        at ListTestGroupPage
    }

    void "projects dropdown redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).navBar.goToProjectDomain("Projects")

        then:
        at ListProjectPage
    }

    void "projects dropdown home redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).navBar.goToProjectDomain("Project Home")

        then:
        at ProjectHomePage
    }
}
