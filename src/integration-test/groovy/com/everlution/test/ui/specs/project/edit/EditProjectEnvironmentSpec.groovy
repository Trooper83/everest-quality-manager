package com.everlution.test.ui.specs.project.edit

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditProjectEnvironmentSpec extends GebSpec {

    EnvironmentService environmentService
    BugService bugService
    PersonService personService
    ProjectService projectService
    ScenarioService scenarioService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "environment tag can be added to existing project"() {
        given: "get a project"
        def id = projectService.list(max: 1).first().id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        EditProjectPage page = browser.page(EditProjectPage)
        page.addEnvironmentTag("Added environment tag")
        page.editProject()

        then: "environment tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isEnvironmentDisplayed("Added environment tag")
    }

    void "environment tag can be edited on existing project"() {
        given: "save a project"
        def pd = DataFactory.project()
        def ed = DataFactory.environment()
        def env = new Environment(ed)
        def project = new Project(name: pd.name, code: pd.code, environments: [env])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to show page"
        go "/project/show/${id}"

        expect: "environment tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isEnvironmentDisplayed(ed.name)

        when: "edit the project"
        showPage.goToEdit()
        EditProjectPage page = browser.page(EditProjectPage)
        page.editEnvironmentTag(ed.name, "edited environment tag")
        page.editProject()

        then: "environment tag is displayed"
        showPage.isEnvironmentDisplayed("edited environment tag")
        !showPage.isEnvironmentDisplayed(ed.name)
    }

    void "tooltip displays for editing existing environment with blank name"() {
        given: "save a project"
        def pd = DataFactory.project()
        def ed = DataFactory.environment()
        def env = new Environment(ed)
        def project = new Project(name: pd.name, code: pd.code, environments: [env])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "edit the project"
        EditProjectPage page = browser.page(EditProjectPage)
        page.editEnvironmentTag(env.name, "")

        then: "environment tag tooltip is displayed"
        page.tooltip.displayed
    }

    void "environment tag with associated test case cannot be deleted from existing project"() {
        given: "save a project"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, environments: [env])
        def tc = DataFactory.testCase()
        def testCase = new TestCase(person: person, name: tc.name, description: tc.description,
                project: project, environments: [env], executionMethod: tc.executionMethod, type: tc.type)
        def id = projectService.save(project).id
        testCaseService.save(testCase)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the environment tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag(env.name)
        page.editProject()

        then: "environment tag is displayed and was not deleted"
        page.errorMessages.text() == "Removed entity has associated items and cannot be deleted"
        environmentService.get(env.id) != null
    }

    void "environment tag with associated bug cannot be deleted from existing project"() {
        given: "save a project"
        def environment = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, environments: [environment])
        def bd = DataFactory.bug()
        def bug = new Bug(name: bd.name, person: person, project: project, environments: [environment])
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the environment tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag(environment.name)
        page.editProject()

        then: "environment tag is displayed and was not deleted"
        page.errorMessages.text() == "Removed entity has associated items and cannot be deleted"
        environmentService.get(environment.id) != null
    }

    void "environment tag with associated scenario cannot be deleted from existing project"() {
        given: "save a project"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, environments: [env])
        def sd = DataFactory.scenario()
        def scn = new Scenario(name: "delete scenario", person: person, project: project,
                executionMethod: "Manual", type: "UI", environments: [env])
        def id = projectService.save(project).id
        scenarioService.save(scn)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the environment tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag(env.name)
        page.editProject()

        then: "environment tag is displayed and was not deleted"
        page.errorMessages.text() == "Removed entity has associated items and cannot be deleted"
        environmentService.get(env.id) != null
    }

    void "project data repopulated when environment tag fails to be removed"() {
        given: "save a project"
        def environment = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, environments: [environment])
        def bd = DataFactory.bug()
        def bug = new Bug(name: bd.name, person: person, project: project, environments: [environment])
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the environment tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag(environment.name)
        page.editProject()

        then: "data is displayed"
        page.isEnvironmentTagDisplayed(environment.name)
        page.nameInput.value() == project.name
        page.codeInput.value() == project.code
    }

    void "environment tag with no association can be deleted from existing project"() {
        given: "save a project"
        def environment = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, environments: [environment])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to show page"
        go "/project/show/${id}"

        expect: "environment to be found"
        environmentService.get(environment.id) != null

        and: "environment tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isEnvironmentDisplayed(environment.name)

        when: "remove the environment tag"
        showPage.goToEdit()
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag(environment.name)
        page.editProject()

        then: "environment tag is displayed and was deleted"
        !showPage.isEnvironmentDisplayed(environment.name)
        environmentService.get(environment.id) == null
    }

    void "Removed item input added when existing environment tag removed"() {
        given: "project with tag"
        def env = new Environment(DataFactory.environment())
        def project = new Project(name: "Environment Tag Removed Input Project II", code: "ATR",
                environments: [env])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.PROJECT_ADMIN.username, "password")

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "remove the environment tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag(env.name)

        then: "removed input is added to dom"
        page.environmentRemovedInput.size() == 1
    }
}
