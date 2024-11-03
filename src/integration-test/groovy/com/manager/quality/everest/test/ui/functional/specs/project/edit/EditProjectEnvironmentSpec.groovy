package com.manager.quality.everest.test.ui.functional.specs.project.edit

import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Environment
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.Scenario
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.services.environment.EnvironmentService
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.services.scenario.ScenarioService
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.EditProjectPage
import com.manager.quality.everest.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
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
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

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
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

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
        def bug = new Bug(name: bd.name, person: person, project: project, environments: [environment], status: "Open",
                actual: "actual", expected: "expected")
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

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
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

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
        def bug = new Bug(name: bd.name, person: person, project: project, environments: [environment], status: "Open",
                actual: "actual", expected: "expected")
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

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
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

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
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "remove the environment tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag(env.name)

        then: "removed input is added to dom"
        page.environmentRemovedInput.size() == 1
    }

    void "removed env tags are not persisted"() {
        given: "project with tag"
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, environments: [new Environment(name: "Env Name"),
                                                                               new Environment(name: "Env Name1")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        and: "remove the area tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removeEnvironmentTag("Env Name")
        page.addEnvironmentTag("Env Name2")

        when:
        page.editProject()

        then:
        def show = browser.page(ShowProjectPage)
        !show.isEnvironmentDisplayed("Env Name")
        show.isEnvironmentDisplayed("Env Name1")
        show.isEnvironmentDisplayed("Env Name2")
    }
}
