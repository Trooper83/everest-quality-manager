package com.everlution.test.ui.specs.bug.edit

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Environment
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageEnvironmentsSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    @Shared Person person

    def setup() {
        person = personService.list(max: 1).first()
    }

    void "environment select populates with only elements within the associated project"() {
        setup: "project & bug instances with environments"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(
                new Project(name: pd.name, code: pd.code, environments: [env])
        )
        def bug = bugService.save(
                new Bug(name: "environment testing bug", project: project, person: person)
        )

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${bug.id}"

        then: "environment populates with project.environments"
        EditBugPage page = browser.page(EditBugPage)
        page.environmentsOptions*.text() == ["--No Environment--", env.name]
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
                        environments: [env, env1]
                )
        )

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${bug.id}"

        then: "bug.environment is selected"
        EditBugPage page = browser.page(EditBugPage)
        page.environmentsSelect().selectedText == [env.name, env1.name]
    }

    void "environment select defaults no selection when no environment set"() {
        setup: "project & bug instances with environments"
        def pd = DataFactory.project()
        def project = projectService.save(
                new Project(name: pd.name, code: pd.code)
        )
        def bug = bugService.save(
                new Bug(name: "environment testing bug", project: project, person: person)
        )

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        when: "go to edit page"
        go "/bug/edit/${bug.id}"

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
                environments: [env]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/bug/edit/${bug.id}"

        and: "bug.environment is set to null"
        EditBugPage page = browser.page(EditBugPage)
        page.environmentsSelect().selected = [""]

        expect: "env set to null"
        page.environmentsSelect().selectedText == ["--No Environment--"]

        when: "add empty steps"
        page.stepsTable.addStep("", "")

        and: "submit"
        page.editBug()

        then: "at the edit page"
        at EditBugPage
    }
}
