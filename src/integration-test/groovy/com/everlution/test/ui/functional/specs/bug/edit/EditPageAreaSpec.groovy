package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.Area
import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageAreaSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "exception handled when validation error present and area set to null"() {
        given: "project & bug instances with areas"
        def area = new Area(name: "area testing area II")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def bug = bugService.save(new Bug(name: "area testing bug II", project: project, person: person,
                area: area, status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "project/${project.id}/bug/edit/${bug.id}"

        and: "bug.area is set to null"
        EditBugPage page = browser.page(EditBugPage)
        page.areaSelect().selected = ""

        expect: "area set to null"
        page.areaSelect().selectedText == ""

        when: "add empty steps"
        page.scrollToBottom()
        page.stepsTable.addStep("", "")

        and: "submit"
        page.edit()

        then: "at the edit page"
        at EditBugPage
    }
}