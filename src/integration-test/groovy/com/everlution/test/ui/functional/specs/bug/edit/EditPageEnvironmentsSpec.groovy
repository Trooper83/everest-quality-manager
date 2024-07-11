package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.domains.Bug
import com.everlution.services.bug.BugService
import com.everlution.domains.Environment
import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
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
class EditPageEnvironmentsSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "environment select defaults with multiple selected environment"() {
        setup: "project & bug instances with environments"
        def env = new Environment(DataFactory.environment())
        def env1 = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(
                new Project(name: pd.name, code: pd.code, environments: [env, env1])
        )
        def bug = bugService.save(
                new Bug(name: "environment testing bug II", project: project, person: person,
                        environments: [env, env1], status: "Open", actual: "actual", expected: "expected"
                )
        )

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "project/${project.id}/bug/edit/${bug.id}"

        then: "bug.environment is selected"
        EditBugPage page = browser.page(EditBugPage)
        page.environmentsSelect().selectedText.containsAll(env.name, env1.name)
    }

    void "environment select defaults no selection when no environment set"() {
        setup: "project & bug instances with environments"
        def pd = DataFactory.project()
        def project = projectService.save(
                new Project(name: pd.name, code: pd.code)
        )
        def bug = bugService.save(
                new Bug(name: "environment testing bug", project: project, person: person, status: "Open",
                        actual: "actual", expected: "expected")
        )

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when: "go to edit page"
        go "project/${project.id}/bug/edit/${bug.id}"

        then: "environment defaults with no selection"
        EditBugPage page = browser.page(EditBugPage)
        page.environmentsSelect().selectedText.empty
    }

    void "exception handled when validation error present and environment set to null"() {
        given: "project & bug instances with areas"
        def env = new Environment(name: "area testing env I")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def bug = bugService.save(new Bug(name: "env testing bug II", project: project, person: person,
                environments: [env], status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "project/${project.id}/bug/edit/${bug.id}"

        and: "bug.environment is set to null"
        EditBugPage page = browser.page(EditBugPage)
        page.environmentsSelect().selected = [""]

        expect: "env set to null"
        page.environmentsSelect().selectedText == ["No Environment..."]

        when: "add empty steps"
        page.scrollToBottom()
        page.stepsTable.addStep("", "","")

        and: "submit"
        page.edit()

        then: "at the edit page"
        at EditBugPage
    }

    void "environment options equal project options"() {
        given: "project & bug instances with areas"
        def env = new Environment(name: "area testing env I")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code, environments: [env]))
        def bug = bugService.save(new Bug(name: "env testing bug II", project: project, person: person,
                environments: [env], status: "Open", actual: "actual", expected: "expected"))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to EditBugPage, project.id, bug.id

        then:
        page.environmentsOptions*.text().containsAll(env.name)
    }
}
