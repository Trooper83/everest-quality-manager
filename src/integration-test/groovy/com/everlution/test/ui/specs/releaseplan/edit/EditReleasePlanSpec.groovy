package com.everlution.test.ui.specs.releaseplan.edit

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.releaseplan.EditReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditReleasePlanSpec extends GebSpec {

    ProjectService projectService
    ReleasePlanService releasePlanService

    void "authorized users can edit plan"(String username, String password) {
        setup: "get instance"
        def id = releasePlanService.list(max: 1).first().id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(username, password)

        and: "go to edit page"
        go "/releasePlan/edit/${id}"

        when: "edit the instance"
        def page = browser.page(EditReleasePlanPage)
        page.editReleasePlan()

        then: "at show page with message displayed"
        at ShowReleasePlanPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }

    void "all edit from data saved"() {
        setup: "setup data"
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code))
        def pd = DataFactory.releasePlan()
        def plan = new ReleasePlan(name: pd.name, project: project)
        def id = releasePlanService.save(plan).id

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/releasePlan/edit/${id}"

        when: "edit instance"
        def editPage = at EditReleasePlanPage
        editPage.editReleasePlan("edited name")

        then: "at show page with edited data"
        def showPage = at ShowReleasePlanPage
        showPage.nameValue.text() == "edited name"
    }
}
