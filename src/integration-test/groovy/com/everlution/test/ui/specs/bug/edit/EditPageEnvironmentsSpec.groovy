package com.everlution.test.ui.specs.bug.edit

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Environment
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditPageEnvironmentsSpec extends GebSpec {

    BugService bugService
    ProjectService projectService

    void "environment select populates with only elements within the associated project"() {
        setup: "project & bug instances with environments"
        def env = new Environment(DataFactory.environment())
        def pd = DataFactory.project()
        def project = projectService.save(
                new Project(name: pd.name, code: pd.code, environments: [env])
        )
        def bug = bugService.save(
                new Bug(name: "environment testing bug", project: project, creator: "testing")
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
                new Bug(name: "environment testing bug II", project: project, creator: "testing",
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
                new Bug(name: "environment testing bug", project: project, creator: "testing")
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
}
