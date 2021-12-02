package com.everlution.test.ui.specs.bug.edit

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.TestStepService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class EditBugStepsSpec extends GebSpec {

    BugService bugService
    ProjectService projectService
    TestStepService testStepService

    void "step can be added to existing bug"() {
        setup: "create bug"
        def project = projectService.list(max: 1).first()
        def bug = new Bug(creator: "bug creator", name: "name of bug", project: project,
                steps: [new Step(action: "bug action", result: "bug result")])
        def id = bugService.save(bug).id

        and: "login as a basic user"
        def loginPage = to LoginPage
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/bug/edit/${id}"

        when: "edit the bug"
        EditBugPage page = browser.page(EditBugPage)
        page.stepsTable.addStep("added step", "added result")
        page.editBug()

        then: "at show page"
        def show = at ShowBugPage
        show.stepsTable.isRowDisplayed("added step", "added result")
        show.stepsTable.getRowCount() == 2
    }

    void "step can be updated on existing bug"() {
        setup: "create bug"
        def project = projectService.list(max: 1).first()
        def bug = new Bug(creator: "bug creator", name: "name of bug", project: project,
                steps: [new Step(action: "bug action", result: "bug result")])
        def id = bugService.save(bug).id

        and: "login as a basic user"
        def loginPage = to LoginPage
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/bug/edit/${id}"

        when: "edit the bug"
        def page = browser.page(EditBugPage)
        page.stepsTable.editTestStep(0, "edited action", "edited result")
        page.editBug()

        then: "at show page with edited step"
        def showPage = at ShowBugPage
        showPage.stepsTable.isRowDisplayed("edited action", "edited result")
    }

    void "step can be deleted from existing bug"() {
        setup: "create bug"
        def project = projectService.list(max: 1).first()
        def step = new Step(action: "bug action", result: "bug result")
        def bug = new Bug(creator: "bug creator", name: "name of bug", project: project,
                steps: [step])
        def id = bugService.save(bug).id

        and: "login as a basic user"
        def loginPage = to LoginPage
        loginPage.login(Usernames.BASIC.username, "password")

        and: "go to edit page"
        go "/bug/edit/${id}"

        when: "edit the bug"
        def page = browser.page(EditBugPage)
        page.stepsTable.removeRow(0)
        page.editBug()

        then: "at show page with edited step"
        def showPage = at ShowBugPage
        !showPage.stepsTable.isRowDisplayed("bug action", "bug result")
        showPage.stepsTable.getRowCount() == 0
    }
}
