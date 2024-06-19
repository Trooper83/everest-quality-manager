package com.everlution.test.ui.functional.specs.stepTemplate

import com.everlution.domains.Bug
import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.Step
import com.everlution.domains.StepTemplate
import com.everlution.domains.TestCase
import com.everlution.services.bug.BugService
import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.services.step.StepService
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.services.testcase.TestCaseService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.stepTemplate.EditStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class RelatedItemsSpec extends GebSpec {

    @Shared Person person
    @Shared Project project

    BugService bugService
    PersonService personService
    ProjectService projectService
    StepService stepService
    StepTemplateService stepTemplateService
    TestCaseService testCaseService

    def setup() {
        project = projectService.list(max:1).first()
        person = personService.list(max:1).first()
    }

    void "updating steptemplate action and result updates bug steps"() {
        given:
        def s = new StepTemplate(name: "Editing step template", act: "og act", result: "og result",
                project: project, person: person)
        stepTemplateService.save(s)
        def step = new Step(act: "og act", result: "og result", isBuilderStep: true, template: s)
        def b = new Bug(name: "bug name", steps: [step], project: project, person: person, status: "Open")
        bugService.save(b)

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def stepPage = to EditStepTemplatePage, project.id, s.id
        stepPage.editStepTemplate("new act", "Editing step template", "new result")

        when:
        def showPage = to ShowBugPage, project.id, b.id

        then:
        showPage.isStepsRowDisplayed("new act", "", "new result")
    }

    void "updating steptemplate action and result updates testcase steps"() {
        given:
        def s = new StepTemplate(name: "Editing test case step template", act: "og act", result: "og result",
                project: project, person: person)
        stepTemplateService.save(s)
        def step = new Step(act: "og act", result: "og result", isBuilderStep: true, template: s)
        def t = new TestCase(name: "test case name", steps: [step], project: project, person: person)
        testCaseService.save(t)

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def stepPage = to EditStepTemplatePage, project.id, s.id
        stepPage.editStepTemplate("new act", "Editing test case step template", "new result")

        when:
        def showPage = to ShowTestCasePage, project.id, t.id

        then:
        showPage.isStepsRowDisplayed("new act", "", "new result")
    }

    void "deleting steptemplate sets bug steps template id to null and step still exists on bug"() {
        given:
        def s = new StepTemplate(name: "Editing bug null step template", act: "og act", result: "og result",
                project: project, person: person)
        stepTemplateService.save(s)
        def step = new Step(act: "og act", result: "og result", isBuilderStep: true, template: s)
        def b = new Bug(name: "Deleting template still works with bug name", steps: [step], project: project, person: person, status: "Open")
        bugService.save(b)

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def showTemplate = to ShowStepTemplatePage, project.id, s.id
        showTemplate.delete()

        expect:
        stepTemplateService.get(s.id) == null

        when:
        def showPage = to ShowBugPage, project.id, b.id

        then:
        showPage.isStepsRowDisplayed("og act", "", "og result")
        stepService.get(step.id).template == null
    }

    void "deleting steptemplate sets testcase steps template id to null and step still exists on test case"() {
        given:
        def s = new StepTemplate(name: "Editing test case null step template", act: "og act", result: "og result",
                project: project, person: person)
        stepTemplateService.save(s)
        def step = new Step(act: "og act", result: "og result", isBuilderStep: true, template: s)
        def t = new TestCase(name: "Deleting template still works with test name", steps: [step], project: project,
                person: person)
        testCaseService.save(t)

        and:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def showTemplate = to ShowStepTemplatePage, project.id, s.id
        showTemplate.delete()

        expect:
        stepTemplateService.get(s.id) == null

        when:
        def showPage = to ShowTestCasePage, project.id, t.id

        then:
        showPage.isStepsRowDisplayed("og act", "", "og result")
        stepService.get(step.id).template == null
    }
}
