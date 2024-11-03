package com.manager.quality.everest.test.ui.functional.specs.bug.edit

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.bug.EditBugPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
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
        page.stepsTable.addStep("", "","")

        and: "submit"
        page.edit()

        then: "at the edit page"
        at EditBugPage
    }

    void "area defaults with no value when area null"() {
        given:
        def area = new Area(name: "area testing area II434")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def bug = bugService.save(new Bug(name: "area testing bug II", project: project, person: person,
                status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditBugPage, project.id, bug.id

        then:
        page.areaSelect().selectedText == ""
    }

    void "area value set when area value not blank"() {
        given:
        def area = new Area(name: "area testing area II43434324")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def bug = bugService.save(new Bug(name: "area testing bug II", project: project, person: person,
                area: area, status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditBugPage, project.id, bug.id

        then:
        page.areaSelect().selectedText == area.name
    }

    void "area options equal project options"() {
        given:
        def area = new Area(name: "area testing area II43434324556565")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, areas: [area]))
        def bug = bugService.save(new Bug(name: "area testing bug II", project: project, person: person,
                area: area, status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditBugPage, project.id, bug.id

        then:
        page.areaOptions*.text().containsAll(area.name)
    }
}
