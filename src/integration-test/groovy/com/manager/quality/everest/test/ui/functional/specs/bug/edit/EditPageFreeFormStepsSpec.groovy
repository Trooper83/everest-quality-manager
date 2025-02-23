package com.manager.quality.everest.test.ui.functional.specs.bug.edit

import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.services.bug.BugService
import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.bug.EditBugPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class EditPageFreeFormStepsSpec extends GebSpec {

    BugService bugService
    PersonService personService
    ProjectService projectService

    def setup() {
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def data = DataFactory.bug()
        def bug = new Bug(name: data.name, status: 'Open', project: project, person: person,
                steps: [new Step(act: 'action 1', result: 'result 1')])
        bugService.save(bug)

        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        to(EditBugPage, project.id, bug.id)
    }

    void "add test step row"() {
        when: "add a test step row"
        EditBugPage page = at EditBugPage
        page.scrollToBottom()
        page.stepsTable.addRow()

        then: "row count is 2"
        page.stepsTable.getStepsCount() == 2
    }

    void "remove test step row"() {
        given: "add a row"
        def page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addRow()

        expect: "row count is 2"
        page.stepsTable.getStepsCount() == 2

        when: "remove the second row"
        page.stepsTable.removeRow(1)

        then: "row count is 1"
        page.stepsTable.getStepsCount() == 1
    }

    void "removing step adds hidden input"() {
        when: "remove step"
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.removeRow(0)

        then: "hidden input added"
        page.stepRemovedInput.size() == 1
    }

    void "remove button only displayed for last step"() {
        given: "add a row"
        def page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addRow()

        expect:
        def count = page.stepsTable.getStepsCount() - 1
        page.stepsTable.getStep(count).find('input[value=Remove]').displayed

        when:
        page.stepsTable.addRow()

        then:
        !page.stepsTable.getStep(count).find('input[value=Remove]').displayed
        page.stepsTable.getStep(count + 1).find('input[value=Remove]').displayed
    }
}
