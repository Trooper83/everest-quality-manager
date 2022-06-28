package com.everlution.test.ui.specs.person

import com.everlution.Person
import com.everlution.PersonService
import com.everlution.SpringSecurityUiService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.SecurityRoles
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.person.ProfilePage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class ProfileSpec extends GebSpec {

    PersonService personService
    SpringSecurityUiService springSecurityUiService

    void "profile link goes to my profile"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        when:
        browser.at(ListProjectPage).navBar.goToProfile()

        then:
        def page = at ProfilePage
        page.emailInput.text == Usernames.READ_ONLY.username
    }

    void "anonymous user cannot view profile"() {
        when:
        go "/user/profile"

        then:
        at LoginPage
    }

    void "user password is encrypted and update message displayed"() {
        given:
        def p = DataFactory.person()
        def person = springSecurityUiService.saveUser([email: p.email],[], p.password) as Person
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(p.email, p.password)

        and:
        go "user/profile"

        when:
        ProfilePage page = browser.page(ProfilePage)
        page.updatePassword("thisismynewpassword")

        then:
        page.statusMessage.text() == "Password updated."
        personService.findByEmail(person.email).password.startsWith("{bcrypt}")
    }

    void "user can login with new credentials"() {
        given:
        def p = DataFactory.person()
        springSecurityUiService.saveUser([email: p.email],[SecurityRoles.ROLE_READ_ONLY.role], p.password) as Person
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(p.email, p.password)

        and:
        go "/user/profile"

        when:
        ProfilePage page = browser.page(ProfilePage)
        page.updatePassword("thisismynewpassword")

        and:
        page.navBar.logout()

        and:
        def logPage = at LoginPage
        logPage.login(p.email, "thisismynewpassword")

        then:
        at ListProjectPage
    }

    void "user cannot login with old password"() {
        given:
        def p = DataFactory.person()
        springSecurityUiService.saveUser([email: p.email],[], p.password) as Person
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(p.email, p.password)

        and:
        go "/user/profile"

        when:
        ProfilePage page = browser.page(ProfilePage)
        page.updatePassword("thisismynewpassword")

        and:
        page.navBar.logout()

        and:
        def logPage = at LoginPage
        logPage.login(p.email, p.password)

        then:
        waitFor {
            logPage.loginFailureMessage.text() == "Sorry, we were not able to find a user with that username and password."
        }
    }

    void "error message displayed for duplicate form submission"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and:
        go "/user/profile"

        when:
        ProfilePage page = browser.page(ProfilePage)
        interact {
            doubleClick(page.updateButton)
        }

        then:
        page.errorMessage.text() == 'An error has occurred, please try again.'
    }

    void "user error message displayed when passwords do not match"() {
        given:
        def p = DataFactory.person()
        springSecurityUiService.saveUser([email: p.email],[], p.password) as Person
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(p.email, p.password)

        and:
        go "user/profile"

        when:
        ProfilePage page = browser.page(ProfilePage)
        page.passwordInput.text = "thisismynewpassword"
        page.confirmPasswordInput.text = "failed"
        page.updateButton.click()

        then:
        page.errorMessage.text() == "Passwords must match."
    }

    void "user error message displayed for invalid password"() {
        given:
        def p = DataFactory.person()
        springSecurityUiService.saveUser([email: p.email],[], p.password) as Person
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(p.email, p.password)

        and:
        go "user/profile"

        when:
        ProfilePage page = browser.page(ProfilePage)
        page.updatePassword("password")

        then:
        page.errorMessage.text() ==
                "Property [password] of class [class com.everlution.Person] with value [password] does not match the required pattern [^.*(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&]).*\$]"
    }

    void "user error message displayed for too short password"() {
        given:
        def p = DataFactory.person()
        springSecurityUiService.saveUser([email: p.email], [], p.password) as Person
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(p.email, p.password)

        and:
        go "user/profile"

        when:
        ProfilePage page = browser.page(ProfilePage)
        page.updatePassword("passwor")

        then:
        page.errorMessage.displayed
    }
}
