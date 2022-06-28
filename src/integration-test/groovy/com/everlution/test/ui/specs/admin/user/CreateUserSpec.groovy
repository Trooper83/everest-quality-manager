package com.everlution.test.ui.specs.admin.user

import com.everlution.PersonService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.SecurityRoles
import com.everlution.test.ui.support.data.UserStatuses
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.admin.user.CreateUserPage
import com.everlution.test.ui.support.pages.admin.user.EditUserPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateUserSpec extends GebSpec {

    PersonService personService

    void "newly created user can login"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to CreateUserPage

        and:
        def person = DataFactory.person()
        page.createPerson(person.email, person.password, [], [SecurityRoles.ROLE_BASIC.role])

        and: //TODO: needed due to logout not working on create user view, remove once bootstrapped
        def project = to ListProjectPage
        project.navBar.logout()

        when:
        def logPage = at LoginPage
        logPage.login(person.email, person.password)

        then:
        at ListProjectPage
    }

    void "error message displayed with non-unique email"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to CreateUserPage

        when:
        page.createPerson(Usernames.BASIC.username, "password", [], [])

        then:
        page.errorMessage.text() == "Property [email] of class [class com.everlution.Person] with value [basic@basic.com] must be unique"
    }

    void "error message displayed for null email"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to CreateUserPage

        when:
        page.createPerson("", "", [], [])

        then:
        def errors = page.errorMessage*.text()
        errors.contains("Property [email] of class [class com.everlution.Person] cannot be null")
        errors.contains("Property [password] of class [class com.everlution.Person] cannot be null")
    }

    void "user attributes are persisted and displayed on edit view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        def page = to CreateUserPage

        when:
        page.createPerson("user@email.com", "password",
                [UserStatuses.ACCOUNT_LOCKED.status], //ENABLED selected by default
                [SecurityRoles.ROLE_BASIC.role, SecurityRoles.ROLE_APP_ADMIN.role])

        then:
        def edit = at EditUserPage
        edit.areRolesSelected([SecurityRoles.ROLE_BASIC.role, SecurityRoles.ROLE_APP_ADMIN.role])
        edit.areStatusesSelected([UserStatuses.ENABLED.status, UserStatuses.ACCOUNT_LOCKED.status])
        edit.emailInput.text == "user@email.com"
    }

    void "user password is encrypted"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        when:
        def page = to CreateUserPage
        def person = DataFactory.person()
        page.createPerson(person.email, person.password, [], [])

        then:
        def p = personService.findByEmail(person.email)
        p.password.startsWith("{bcrypt}")
    }

    void "error message displayed with invalid password"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "!Password#2022")

        when:
        def page = to CreateUserPage
        def person = DataFactory.person()
        page.createPerson(person.email, "password", [], [])

        then:
        page.errorMessage.text() ==
                "Property [password] of class [class com.everlution.Person] with value [password] does not match the required pattern [^.*(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&]).*\$]"
    }

    void "error message displayed with too small password"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "!Password#2022")

        when:
        def page = to CreateUserPage
        def person = DataFactory.person()
        page.createPerson(person.email, "passwor", [], [])

        then:
        def messages = page.errorMessage*.text()
        messages.contains("Property [password] of class [class com.everlution.Person] with value [passwor] does not match the required pattern [^.*(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&]).*\$]")
        messages.contains("Property [password] of class [class com.everlution.Person] with value [passwor] does not fall within the valid size range from [8] to [256]")
    }
}