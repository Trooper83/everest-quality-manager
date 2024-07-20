package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.domains.Bug
import com.everlution.domains.Person
import com.everlution.domains.Platform
import com.everlution.domains.Project
import com.everlution.services.bug.BugService
import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPagePlatformSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "exception handled when validation error present and platform set to null"() {
        given: "project & bug instances with areas"
        def platform = new Platform(name: "platform testing area II")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def bug = bugService.save(new Bug(name: "platform testing bug II", project: project, person: person,
                platform: platform, status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "project/${project.id}/bug/edit/${bug.id}"

        and:
        EditBugPage page = browser.page(EditBugPage)
        page.platformSelect().selected = ""

        expect: "platform set to null"
        page.platformSelect().selectedText == ""

        when: "add empty steps"
        page.nameInput = ""

        and: "submit"
        page.edit()

        then: "at the edit page"
        at EditBugPage
    }

    void "platform options equal project items"() {
        given: "project & bug instances with areas"
        def platform = new Platform(name: "platform testing area II76765")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def bug = bugService.save(new Bug(name: "platform testing bug II", project: project, person: person,
                platform: platform, status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditBugPage, project.id, bug.id

        then:
        page.platformOptions*.text().containsAll(platform.name)
    }

    void "platform blank when no value set"() {
        given: "project & bug instances with areas"
        def platform = new Platform(name: "platform testing area II76765")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def bug = bugService.save(new Bug(name: "platform testing bug II", project: project, person: person,
                status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditBugPage, project.id, bug.id

        then:
        page.platformSelect().selectedText == ""
    }

    void "platform value set when platform not blank"() {
        given: "project & bug instances with areas"
        def platform = new Platform(name: "platform testing area II76765")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, platforms: [platform]))
        def bug = bugService.save(new Bug(name: "platform testing bug II", project: project, person: person,
                platform: platform, status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditBugPage, project.id, bug.id

        then:
        page.platformSelect().selectedText == platform.name
    }
}
