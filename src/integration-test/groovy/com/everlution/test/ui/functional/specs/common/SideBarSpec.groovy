package com.everlution.test.ui.functional.specs.common

import com.everlution.test.ui.support.data.Credentials
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
class SideBarSpec extends GebSpec {

    void "bugs redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).sideBar.goToProjectDomain("Bugs")

        then:
        at ListBugPage
    }

    void "release plan redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).sideBar.goToProjectDomain("Release Plans")

        then:
        at ListReleasePlanPage
    }

    void "scenario redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).sideBar.goToProjectDomain("Scenarios")

        then:
        at ListScenarioPage
    }

    void "test case redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).sideBar.goToProjectDomain("Test Cases")

        then:
        at ListTestCasePage
    }

    void "test group redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).sideBar.goToProjectDomain("Test Groups")

        then:
        at ListTestGroupPage
    }

    void "project name redirects to correct page"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        and:
        browser.at(ListProjectPage).projectTable.clickCell("Name", 0)

        when:
        browser.at(ProjectHomePage).sideBar.goToProjectHome()

        then:
        at ProjectHomePage
    }
}
