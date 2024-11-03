package com.manager.quality.everest.test.ui.functional.specs.project.edit


import com.manager.quality.everest.services.platform.PlatformService
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase
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
class EditProjectPlatformSpec extends GebSpec {

    PlatformService platformService
    BugService bugService
    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "platform tag can be added to existing project"() {
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
        page.addPlatformTag("Added platform tag")
        page.editProject()

        then: "platform tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isPlatformDisplayed("Added platform tag")
    }

    void "platform tag with associated test case cannot be deleted from existing project"() {
        given: "save a project"
        def platform = new Platform(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, platforms: [platform])
        def tc = DataFactory.testCase()
        def testCase = new TestCase(person: person, name: tc.name, description: tc.description,
                project: project, platform: platform, executionMethod: tc.executionMethod, type: tc.type)
        def id = projectService.save(project).id
        testCaseService.save(testCase)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the platform tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removePlatformTag(platform.name)
        page.editProject()

        then: "platform tag is displayed and was not deleted"
        page.errorMessages.text() == "Removed entity has associated items and cannot be deleted"
        platformService.get(platform.id) != null
    }

    void "platform tag with associated bug cannot be deleted from existing project"() {
        given: "save a project"
        def platform = new Platform(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, platforms: [platform])
        def bd = DataFactory.bug()
        def bug = new Bug(name: bd.name, person: person, project: project, platform: platform, status: "Open", actual: "actual",
                expected: "expected")
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the platform tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removePlatformTag(platform.name)
        page.editProject()

        then: "platform tag is displayed and was not deleted"
        page.errorMessages.text() == "Removed entity has associated items and cannot be deleted"
        platformService.get(platform.id) != null
    }

    void "project data repopulated when platform tag fails to be removed"() {
        given: "save a project"
        def platform = new Platform(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, platforms: [platform])
        def bd = DataFactory.bug()
        def bug = new Bug(name: bd.name, person: person, project: project, platform: platform, status: "Open",
                actual: "actual", expected: "expected")
        def id = projectService.save(project).id
        bugService.save(bug)

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "attempt to remove the platform tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removePlatformTag(platform.name)
        page.editProject()

        then: "data is displayed"
        page.isPlatformTagDisplayed(platform.name)
        page.nameInput.value() == project.name
        page.codeInput.value() == project.code
    }

    void "platform tag with no association can be deleted from existing project"() {
        given: "save a project"
        def platform = new Platform(DataFactory.area())
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, platforms: [platform])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to show page"
        go "/project/show/${id}"

        expect: "platform to be found"
        platformService.get(platform.id) != null

        and: "platform tag is displayed"
        def showPage = at ShowProjectPage
        showPage.isPlatformDisplayed(platform.name)

        when: "remove the platform tag"
        showPage.goToEdit()
        EditProjectPage page = browser.page(EditProjectPage)
        page.removePlatformTag(platform.name)
        page.editProject()

        then: "platform tag is displayed and was deleted"
        !showPage.isPlatformDisplayed(platform.name)
        platformService.get(platform.id) == null
    }

    void "Removed item input added when existing platform tag removed"() {
        given: "project with tag"
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, platforms: [new Platform(name: "Platform Name")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        when: "remove the platform tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removePlatformTag("Platform Name")

        then: "removed input is added to dom"
        page.platformRemovedInput.size() == 1
    }

    void "removed platform tags are not persisted"() {
        given: "project with tag"
        def pd = DataFactory.project()
        def project = new Project(name: pd.name, code: pd.code, platforms: [new Platform(name: "Platform Name"),
                                                                        new Platform(name: "Platform Name1")])
        def id = projectService.save(project).id

        and: "login as an authorized user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)

        and: "go to edit page"
        go "/project/edit/${id}"

        and: "remove the platform tag"
        EditProjectPage page = browser.page(EditProjectPage)
        page.removePlatformTag("Platform Name")
        page.addPlatformTag("Platform Name2")

        when:
        page.editProject()

        then:
        def show = browser.page(ShowProjectPage)
        !show.isPlatformDisplayed("Platform Name")
        show.isPlatformDisplayed("Platform Name1")
        show.isPlatformDisplayed("Platform Name2")
    }
}
