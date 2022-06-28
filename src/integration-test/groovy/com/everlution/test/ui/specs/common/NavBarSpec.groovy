package com.everlution.test.ui.specs.common

import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.person.ProfilePage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class NavBarSpec extends GebSpec {

    void "navbar links redirect correctly"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.READ_ONLY.username, "password")

        and:
        browser.at(ListProjectPage).navBar.goToProfile()

        expect:
        def page = at ProfilePage

        when:
        page.navBar.goToProjects()

        then:
        at ListProjectPage
    }
}
