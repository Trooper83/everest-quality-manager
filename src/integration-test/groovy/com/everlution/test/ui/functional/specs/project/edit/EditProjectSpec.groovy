package com.everlution.test.ui.functional.specs.project.edit

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditProjectSpec extends GebSpec {

    ProjectService projectService

    void "authorized users can edit project"(String username, String password) {
        given: "get a project"
        def id = projectService.list(max: 1).first().id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        EditProjectPage page = browser.page(EditProjectPage)
        page.editProject()

        then: "at home page"
        at ShowProjectPage

        where:
        username                         | password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }

    void "edited data is saved and displayed"() {
        given: "set data"
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code)
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        def edited = DataFactory.project()
        EditProjectPage page = browser.page(EditProjectPage)
        page.editProject(edited.name, edited.code, [], [])

        then: "at home page and data displayed"
        def showPage = at ShowProjectPage
        verifyAll {
            showPage.nameValue.text() == edited.name
            showPage.codeValue.text() == edited.code
        }
    }
}
