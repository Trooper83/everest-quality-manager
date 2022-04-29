package com.everlution.test.ui.specs.project.create

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class CreateProjectSpec extends GebSpec {

    void "authorized users can create project"(String username, String password, String name, String code) {
        given: "login as a user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to the create page"
        to CreateProjectPage

        when: "create a project"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject(name, code)

        then: "at show page with message displayed"
        at ProjectHomePage

        where:
        username                         | password   | name                | code
        Usernames.PROJECT_ADMIN.username | "password" | "created project 1" | "CP1"
        Usernames.ORG_ADMIN.username     | "password" | "created project 2" | "CP2"
        Usernames.APP_ADMIN.username     | "password" | "created project 3" | "CP3"
    }

    void "all create from data saved"() {
        given: "create data"
        def pd = DataFactory.project()
        def ad = DataFactory.area()
        def ad1 = DataFactory.area()

        and: "login as a user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to the create page"
        def page = to CreateProjectPage

        when: "create a project with an area"
        page.completeCreateForm(pd.name, pd.code)
        page.addAreaTag(ad.name)
        page.addAreaTag(ad1.name)
        page.createButton.click()

        then: "all data is displayed on show page"
        def home = at ProjectHomePage
        verifyAll {
            home.isAreaDisplayed(ad.name)
            home.isAreaDisplayed(ad1.name)
            home.nameValue.text() == pd.name
            home.codeValue.text() == pd.code
        }
    }
}
