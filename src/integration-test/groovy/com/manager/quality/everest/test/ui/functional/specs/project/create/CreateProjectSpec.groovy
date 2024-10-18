package com.manager.quality.everest.test.ui.functional.specs.project.create

import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.CreateProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ShowProjectPage
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
        at ShowProjectPage

        where:
        username                         | password                          | name                | code
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password | "created project 1" | "CP1"
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password     | "created project 3" | "CP3"
    }

    void "all create from data saved"() {
        given: "create data"
        def pd = DataFactory.project()
        def ad = DataFactory.area()
        def ad1 = DataFactory.area()
        def ed = DataFactory.environment()
        def ed1 = DataFactory.environment()
        def pld = DataFactory.area()
        def pld1 = DataFactory.area()

        and: "login as a user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        def page = to CreateProjectPage

        when: "create a project with items"
        page.completeCreateForm(pd.name, pd.code)
        page.addAreaTag(ad.name)
        page.addAreaTag(ad1.name)
        page.addEnvironmentTag(ed.name)
        page.addEnvironmentTag(ed1.name)
        page.addPlatformTag(pld.name)
        page.addPlatformTag(pld1.name)
        page.createButton.click()

        then: "all data is displayed on show page"
        def show = at ShowProjectPage
        verifyAll {
            show.isAreaDisplayed(ad.name)
            show.isAreaDisplayed(ad1.name)
            show.isEnvironmentDisplayed(ed.name)
            show.isEnvironmentDisplayed(ed1.name)
            show.isPlatformDisplayed(pld.name)
            show.isPlatformDisplayed(pld1.name)
            show.nameValue.text() == pd.name
            show.codeValue.text() == pd.code
        }
    }
}
