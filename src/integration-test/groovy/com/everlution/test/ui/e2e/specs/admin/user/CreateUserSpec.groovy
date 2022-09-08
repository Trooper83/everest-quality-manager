package com.everlution.test.ui.e2e.specs.admin.user

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.data.SecurityRoles
import com.everlution.test.ui.support.pages.admin.user.CreateUserPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec

class CreateUserSpec extends GebSpec {

    void "newly created user can login"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.APP_ADMIN.email, Credentials.APP_ADMIN.password)

        and:
        def page = to CreateUserPage

        and:
        def person = DataFactory.person()
        page.createPerson(person.email, person.password, [], [SecurityRoles.ROLE_BASIC.role])
        page.navBar.logout()

        when:
        def logPage = at LoginPage
        logPage.login(person.email, person.password)

        then:
        at ListProjectPage
    }
}
