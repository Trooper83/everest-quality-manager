package com.everlution.test.ui.specs.admin.user

import com.everlution.Person
import com.everlution.PersonService
import com.everlution.SpringSecurityUiService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.SecurityRoles
import com.everlution.test.ui.support.data.UserStatuses
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.admin.user.EditUserPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditUserSpec extends GebSpec {

    PersonService personService
    SpringSecurityUiService springSecurityUiService

    Person person

    def setup() {
        def p = DataFactory.person()
        person = springSecurityUiService.saveUser([email: p.email],[], p.password) as Person

        expect:
        person != null
    }

    void "error message displayed with non-unique email"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        page.editPerson(Usernames.BASIC.username, null, [], [])

        then:
        page.errorMessage.text() == "Property [email] of class [class com.everlution.Person] with value [basic@basic.com] must be unique"
    }

    void "error message displayed for null email"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        page.editPerson(" ", " ", [], [])

        then:
        def errors = page.errorMessage*.text()
        errors.contains("Property [email] of class [class com.everlution.Person] cannot be null")
        errors.contains("Property [password] of class [class com.everlution.Person] cannot be null")
    }

    void "error message displayed with invalid password"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "!Password#2022")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        page.editPerson("user@usingthis.com", "password", [], [])

        then:
        page.errorMessage.text() ==
                "Property [password] of class [class com.everlution.Person] with value [password] does not match the required pattern [^.*(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&]).*\$]"
    }

    void "error message displayed with too short password"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "!Password#2022")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        page.editPerson("user@usingthis.com", "passwor", [], [])

        then:
        def messages = page.errorMessage*.text()
        messages.contains("Property [password] of class [class com.everlution.Person] with value [passwor] does not match the required pattern [^.*(?=.*\\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[!@#\$%^&]).*\$]")
        messages.contains("Property [password] of class [class com.everlution.Person] with value [passwor] does not fall within the valid size range from [8] to [256]")
    }

    void "user attributes are persisted and displayed on edit view"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        page.editPerson("user999@email.com", "password",
                [UserStatuses.ACCOUNT_LOCKED.status, UserStatuses.ENABLED.status],
                [SecurityRoles.ROLE_BASIC.role, SecurityRoles.ROLE_APP_ADMIN.role])

        then:
        def edit = at EditUserPage
        edit.areRolesSelected([SecurityRoles.ROLE_BASIC.role, SecurityRoles.ROLE_APP_ADMIN.role])
        edit.areStatusesSelected([UserStatuses.ACCOUNT_LOCKED.status])
        !edit.areStatusesSelected([UserStatuses.ENABLED.status])
        edit.emailInput.text == "user999@email.com"
    }

    void "user password is encrypted"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        page.editPerson("", "password123", [], [])

        then:
        def p = personService.findByEmail(person.email)
        p.password.startsWith("{bcrypt}")
    }

    void "user can login with new credentials"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        def p = DataFactory.person()
        page.editPerson(p.email, p.password, [], [SecurityRoles.ROLE_READ_ONLY.role])

        and: //TODO: needed due to logout not working on create user view, remove once bootstrapped
        def project = to ListProjectPage
        project.navBar.logout()

        and:
        def logPage = at LoginPage
        logPage.login(p.email, p.password)

        then:
        at ListProjectPage
    }

    void "user cannot login with old password"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.APP_ADMIN.username, "password")

        and:
        go "user/edit/${person.id}"

        when:
        EditUserPage page = browser.page(EditUserPage)
        page.editPerson("", "p123!password", [], [])

        and: //TODO: needed due to logout not working on create user view, remove once bootstrapped
        def project = to ListProjectPage
        project.navBar.logout()

        and:
        def logPage = at LoginPage
        logPage.login(person.email, person.password)

        then:
        waitFor {
            logPage.loginFailureMessage.text() == "Sorry, we were not able to find a user with that username and password."
        }
    }
}
