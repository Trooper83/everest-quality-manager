package com.everlution.test.ui.specs.common

import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testgroup.CreateTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ProjectNavSpec extends GebSpec {

    void "create button hidden for read only users"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.READ_ONLY.email, Credentials.READ_ONLY.password)

        when:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        then:
        at ProjectHomePage
        !browser.page(ProjectHomePage).projectNavButtons.createMenuDropDown.displayed
    }

    void "create button bugs redirect to correct page"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).projectNavButtons.goToCreatePage("Bug")

        then:
        at CreateBugPage
    }

    void "create button release plan redirect to correct page"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).projectNavButtons.goToCreatePage("Release Plan")

        then:
        at CreateReleasePlanPage
    }

    void "create button scenarios redirect to correct page"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).projectNavButtons.goToCreatePage("Scenario")

        then:
        at CreateScenarioPage
    }

    void "create button test case redirect to correct page"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).projectNavButtons.goToCreatePage("Test Case")

        then:
        at CreateTestCasePage
    }

    void "create button test group redirect to correct page"() {
        given: "login as a read only user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 0)

        when:
        browser.page(ProjectHomePage).projectNavButtons.goToCreatePage("Test Group")

        then:
        at CreateTestGroupPage
    }
}
