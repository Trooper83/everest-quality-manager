package com.manager.quality.everest.test.ui.e2e.specs.person


import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.data.SecurityRoles
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.admin.user.CreateUserPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.person.ProfilePage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec

@SendResults
class ProfileSpec extends GebSpec {

    def person

    def setup() {
        person = DataFactory.person()
        to LoginPage
        browser.page(LoginPage).login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)
        def createPage = to CreateUserPage
        createPage.createPerson(person.email, person.password, [], [SecurityRoles.ROLE_READ_ONLY.role])
        waitFor {
            createPage.statusMessage.displayed
        }
        createPage.navBar.logout()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(person.email, person.password)
        to ProfilePage
        ProfilePage page = browser.page(ProfilePage)
        page.updatePassword("This!smynewpassword2022")
        page.navBar.logout()
    }

    void "user can login with new credentials"() {
        when:
        def logPage = at LoginPage
        logPage.login(person.email, "This!smynewpassword2022")

        then:
        at ListProjectPage
    }

    void "user cannot login with old password"() {
        when:
        def logPage = at LoginPage
        logPage.login(person.email, person.password)

        then:
        waitFor {
            logPage.loginFailureMessage.text() == "Sorry, we were not able to find a user with that username and password."
        }
    }
}
