package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditBugStepsSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    @Shared Person person
    @Shared Project project

    def setup() {
        person = personService.list(max: 1).first()
        project = projectService.list(max: 1).first()
    }

    void "step can be added to existing bug"() {
        setup: "create bug"
        def bug = new Bug(person: person, name: "name of bug", project: project, status: "Open",
                steps: [new Step(act: "bug action", result: "bug result")], actual: "actual", expected: "expected")
        def id = bugService.save(bug).id

        and: "login as a basic user"
        def loginPage = to LoginPage
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        when: "edit the bug"
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addStep("added step", "added result")
        page.edit()

        then: "at show page"
        def show = at ShowBugPage
        show.isStepsRowDisplayed("added step", "added result")
        show.getStepsCount() == 2
    }

    void "step can be updated on existing bug"() {
        setup: "create bug"
        def bug = new Bug(person: person, name: "name of bug", project: project, status: "Open",
                steps: [new Step(act: "bug action", result: "bug result")], actual: "actual", expected: "expected")
        def id = bugService.save(bug).id

        and: "login as a basic user"
        def loginPage = to LoginPage
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        when: "edit the bug"
        def page = browser.page(EditBugPage)
        page.stepsTable.editTestStep(0, "edited action", "edited result")
        page.edit()

        then: "at show page with edited step"
        def showPage = at ShowBugPage
        showPage.isStepsRowDisplayed("edited action", "edited result")
    }

    void "step can be deleted from existing bug"() {
        setup: "create bug"
        def step = new Step(act: "bug action", result: "bug result")
        def bug = new Bug(person: person, name: "name of bug", project: project, status: "Open",
                steps: [step], actual: "actual", expected: "expected")
        def id = bugService.save(bug).id

        and: "login as a basic user"
        def loginPage = to LoginPage
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to edit page"
        go "/project/${project.id}/bug/edit/${id}"

        when: "edit the bug"
        def page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.removeRow(0)
        page.edit()

        then: "at show page with edited step"
        def showPage = at ShowBugPage
        !showPage.isStepsRowDisplayed("bug action", "bug result")
        showPage.getStepsCount() == 0
    }
}
