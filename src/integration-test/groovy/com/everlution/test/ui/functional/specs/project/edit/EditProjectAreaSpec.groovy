package com.everlution.test.ui.functional.specs.project.edit

import com.everlution.Area
import com.everlution.AreaService
import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditProjectAreaSpec extends GebSpec {

    AreaService areaService
    BugService bugService
    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "area tag can be added to existing project"() {
        given: "get a project"
        def id = projectService.list(max: 1).first().id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        EditProjectPage page = browser.page(EditProjectPage)
        page.addAreaTag("Added area tag")
        page.editProject()

        then: "area tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isAreaDisplayed("Added area tag")
    }

    void "area tag can be edited on existing project"() {
        given: "save a project"
        def project = new Project(name: "edited project", code: "edn", areas: [new Area(name: "area name")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to show page"
        go "/project/show/${id}"

        expect: "area tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isAreaDisplayed("area name")

        when: "edit the project"
        showPage.goToEdit()
        EditProjectPage page = browser.page(EditProjectPage)
        page.editAreaTag("area name", "edited area tag")
        page.editProject()

        then: "area tag is displayed"
        showPage.isAreaDisplayed("edited area tag")
        !showPage.isAreaDisplayed("area name")
    }

    void "tooltip displays for editing existing area with blank name"() {
        given: "save a project"
        def project = new Project(name: "edited project1", code: "ed1", areas: [new Area(name: "area name")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        EditProjectPage page = browser.page(EditProjectPage)
        page.editAreaTag("area name", "")

        then: "area tag tooltip is displayed"
        page.tooltip.displayed
    }

    void "area tag with associated test case cannot be deleted from existing project"() {
        given: "save a project"
        def area = new Area(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, areas: [area])
        def tc = DataFactory.testCase()
        def testCase = new TestCase(person: person, name: tc.name, description: tc.description,
                project: project, area: area, executionMethod: tc.executionMethod, type: tc.type)
        def id = projectService.save(project).id
        testCaseService.save(testCase)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the area tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeAreaTag(area.name)
        page.editProject()

        then: "area tag is displayed and was not deleted"
        page.errorMessages.text() == "Removed entity has associated items and cannot be deleted"
        areaService.get(area.id) != null
    }

    void "area tag with associated bug cannot be deleted from existing project"() {
        given: "save a project"
        def area = new Area(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, areas: [area])
        def bd = DataFactory.bug()
        def bug = new Bug(name: bd.name, person: person, project: project, area: area, status: "Open", actual: "actual",
                expected: "expected")
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the area tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeAreaTag(area.name)
        page.editProject()

        then: "area tag is displayed and was not deleted"
        page.errorMessages.text() == "Removed entity has associated items and cannot be deleted"
        areaService.get(area.id) != null
    }

    void "project data repopulated when area tag fails to be removed"() {
        given: "save a project"
        def area = new Area(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, areas: [area])
        def bd = DataFactory.bug()
        def bug = new Bug(name: bd.name, person: person, project: project, area: area, status: "Open",
                actual: "actual", expected: "expected")
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the area tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeAreaTag(area.name)
        page.editProject()

        then: "data is displayed"
        page.isAreaTagDisplayed(area.name)
        page.nameInput.value() == project.name
        page.codeInput.value() == project.code
    }

    void "area tag with no association can be deleted from existing project"() {
        given: "save a project"
        def area = new Area(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, areas: [area])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to show page"
        go "/project/show/${id}"

        expect: "area to be found"
        areaService.get(area.id) != null

        and: "area tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isAreaDisplayed(area.name)

        when: "remove the area tag"
        showPage.goToEdit()
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeAreaTag(area.name)
        page.editProject()

        then: "area tag is displayed and was deleted"
        !showPage.isAreaDisplayed(area.name)
        areaService.get(area.id) == null
    }

    void "Removed item input added when existing area tag removed"() {
        given: "project with tag"
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, areas: [new Area(name: "Area Name")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "remove the area tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeAreaTag("Area Name")

        then: "removed input is added to dom"
        page.areaRemovedInput.size() == 1
    }

    void "removed area tags are not persisted"() {
        given: "project with tag"
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, areas: [new Area(name: "Area Name"),
                                                                        new Area(name: "Area Name1")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        and: "remove the area tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeAreaTag("Area Name")
        page.addAreaTag("Area Name2")

        when:
        page.editProject()

        then:
        def show = browser.page(ShowProjectPage)
        !show.isAreaDisplayed("Area Name")
        show.isAreaDisplayed("Area Name1")
        show.isAreaDisplayed("Area Name2")
    }
}
