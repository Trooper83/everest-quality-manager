package com.manager.quality.everest.test.ui.e2e.specs.admin.user


import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.data.SecurityRoles
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.admin.user.CreateUserPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec

@SendResults
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
