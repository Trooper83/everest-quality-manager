package com.manager.quality.everest.test.ui.e2e.specs.project


import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.CreateProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.EditProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ListProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec

@SendResults
class ProjectSpec extends GebSpec {

    void "project is created and data persists"() {
        given: "create data"
        def pd = DataFactory.project()
        def ad = DataFactory.area()
        def pld = DataFactory.area()
        def env = DataFactory.environment()

        and: "login as a user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        def page = to CreateProjectPage

        when: "create a project with an area"
        page.completeCreateForm(pd.name, pd.code)
        page.addAreaTag(ad.name)
        page.addEnvironmentTag(env.name)
        page.addPlatformTag(pld.name)
        page.createButton.click()

        then: "all data is displayed on show page"
        def show = at ShowProjectPage
        verifyAll {
            show.isAreaDisplayed(ad.name)
            show.isEnvironmentDisplayed(env.name)
            show.isPlatformDisplayed(pld.name)
            show.nameValue.text() == pd.name
            show.codeValue.text() == pd.code
        }

        cleanup:
        show.deleteProject()
    }

    void "project is edited and data persists"() {
        given: "login as a user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        def createPage = to CreateProjectPage
        def pd = DataFactory.project()
        createPage.createProject(pd.name, pd.code)

        and:
        browser.page(ShowProjectPage).goToEdit()

        when: "edit the project"
        def edited = DataFactory.project()
        def ad = DataFactory.area()
        def pld = DataFactory.area()
        def env = DataFactory.environment()
        EditProjectPage page = browser.page(EditProjectPage)
        page.editProject(edited.name, edited.code, [ad.name], [env.name], [pld.name])

        then: "at home page and data displayed"
        println('testing')
        def showPage = at ShowProjectPage
        verifyAll {
            showPage.nameValue.text() == edited.name
            showPage.codeValue.text() == edited.code
            showPage.isAreaDisplayed(ad.name)
            showPage.isEnvironmentDisplayed(env.name)
            showPage.isPlatformDisplayed(pld.name)
        }

        cleanup:
        showPage.deleteProject()
    }

    void "project with no items can be deleted"() {
        given: "create data"
        def pd = DataFactory.project()

        and: "login as a user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to the create page"
        def page = to CreateProjectPage

        and: "create a project"
        page.createProject(pd.name, pd.code)

        when: "all data is displayed on show page"
        def show = at ShowProjectPage
        show.deleteProject()

        then:
        def list = at ListProjectPage
        !list.projectTable.isValueInColumn("Name", pd.name)
    }
}
