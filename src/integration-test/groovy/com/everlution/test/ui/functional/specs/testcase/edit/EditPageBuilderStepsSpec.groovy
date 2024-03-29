package com.everlution.test.ui.functional.specs.testcase.edit

import com.everlution.domains.Link
import com.everlution.services.link.LinkService
import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.Relationship
import com.everlution.domains.StepTemplate
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.domains.TestCase
import com.everlution.services.testcase.TestCaseService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.EditTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPageBuilderStepsSpec extends GebSpec {

    LinkService linkService
    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService
    TestCaseService testCaseService

    @Shared
    Person person

    @Shared
    Project project

    @Shared
    StepTemplate stepTemplate

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        project = projectService.list(max: 1).first()
        person = personService.list(max: 1).first()
        stepTemplate = stepTemplateService.findAllInProject(project, [max:1]).results.first()
        def tc = DataFactory.testCase()
        def test = new TestCase(name: tc.name, project: project, person: person)
        testCaseService.save(test)
        to(EditTestCasePage, project.id, test.id)
    }

    void "suggestions only displayed for string of 3 characters or more"(int index) {
        given:
        def text = stepTemplate.name.substring(0, index)

        when:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            page.stepsTable.searchInput << s
        }
        sleep(500)

        then:
        !page.stepsTable.searchResultsMenu.displayed

        where:
        index << [1,2]
    }

    void "suggestions display for string of 3 characters or more"() {
        given:
        def text = stepTemplate.name.substring(0, 3)

        when:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            page.stepsTable.searchInput << s
        }
        sleep(500)

        then:
        page.stepsTable.searchResultsMenu.displayed
    }

    void "steps are retrieved when validation fails"() {
        given:
        def text = stepTemplate.name.substring(0, 5)
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.nameInput = ''
        page.edit()

        when:
        page.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            page.stepsTable.searchInput << s
        }
        sleep(500)

        then:
        page.stepsTable.searchResultsMenu.displayed
    }

    void "adding a step removes the remove link from previous step"() {
        when:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(stepTemplate.name)
        page.stepsTable.addBuilderStep(stepTemplate.name)

        then:
        !page.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed
        page.stepsTable.getBuilderStep(1).find('input[value=Remove]').displayed
    }

    void "removing a step adds a remove link to previous step"() {
        given:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(stepTemplate.name)
        page.stepsTable.addBuilderStep(stepTemplate.name)

        when:
        page.stepsTable.removeBuilderRow(1)

        then:
        page.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed
    }

    void "selecting suggested result displays properties and suggested steps"() {
        setup:
        def s = new StepTemplate(name: "test step", act: "action jackson", result: "result", project: project, person: person)
        stepTemplateService.save(s)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        when:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)

        then:
        page.stepsTable.isSuggestedStepDisplayed(stepTemplate.name)
        page.stepsTable.getCurrentBuilderStepName() == s.name
        page.stepsTable.isBuilderRowDisplayed(s.act, '', s.result)
    }

    void "selecting suggested step displays properties and suggested steps"() {
        setup:
        def s = new StepTemplate(name: "1test step", act: "1action jackson", result: "1result", project: project,
                person: person)
        def st = new StepTemplate(name: "2test step 2", act: "2action jackson 2", result: "2result 2", project: project,
                person: person)
        stepTemplateService.save(s)
        stepTemplateService.save(st)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: stepTemplate.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        when:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)
        page.stepsTable.selectSuggestedStep(stepTemplate.name)

        then:
        page.stepsTable.isSuggestedStepDisplayed(st.name)
        page.stepsTable.getCurrentBuilderStepName() == stepTemplate.name
        page.stepsTable.isBuilderRowDisplayed(stepTemplate.act, '', stepTemplate.result)
    }

    void "suggested steps name and properties are repopulated with current step when step removed"() {
        given:
        def s = new StepTemplate(name: "3test step", act: "3action jackson", result: "3result", project: project, person: person)
        def st = new StepTemplate(name: "4test step 2", act: "4action jackson 2", result: "4result 2", project: project, person: person)
        stepTemplateService.save(s)
        stepTemplateService.save(st)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: stepTemplate.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        and:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)
        page.stepsTable.selectSuggestedStep(stepTemplate.name)

        expect:
        page.stepsTable.isSuggestedStepDisplayed(st.name)
        page.stepsTable.getCurrentBuilderStepName() == stepTemplate.name

        when:
        page.stepsTable.removeBuilderRow(1)

        then:
        page.stepsTable.isSuggestedStepDisplayed(stepTemplate.name)
        page.stepsTable.getCurrentBuilderStepName() == s.name
    }

    void "suggested steps and current step name are displayed upon page load"() {
        given:
        def s = new StepTemplate(name: "33test step", act: "33action jackson", result: "33result", project: project, person: person)
        stepTemplateService.save(s)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)
        page.edit()

        when:
        def show = at ShowTestCasePage
        show.goToEdit()

        then:
        page.stepsTable.isSuggestedStepDisplayed(stepTemplate.name)
        page.stepsTable.getCurrentBuilderStepName() == s.name
    }

    void "form is reset when last step is removed"() {
        given:
        def s = new StepTemplate(name: "6test step", act: "6action jackson", result: "6result", project: project, person: person)
        stepTemplateService.save(s)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)

        expect:
        page.stepsTable.getBuilderStepsCount() == 1
        page.stepsTable.currentStepName.displayed
        page.stepsTable.getSuggestedStepsCount() == 1

        when:
        page.stepsTable.removeBuilderRow(0)

        then:
        page.stepsTable.getBuilderStepsCount() == 0
        !page.stepsTable.currentStepName.displayed
        page.stepsTable.getSuggestedStepsCount() == 0
    }

    void "no results message displayed when no related steps found"() {
        setup:
        def s = new StepTemplate(name: "7test step", act: "7action jackson", result: "7result", project: project, person: person)
        stepTemplateService.save(s)

        when:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)

        then:
        page.stepsTable.noSuggestedStepsText.displayed
    }

    void "builder step row has hidden input"() {
        when:
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(stepTemplate.name)

        then:
        !page.stepsTable.isBuilderStepHiddenInputDisplayed(0)
    }

    void "removing step adds hidden input"() {
        given:
        def st = new StepTemplate(name: 'removing step add hidden input', act: 'action123', project: project, person: person)
        stepTemplateService.save(st)
        def tc = DataFactory.testCase()
        def test = new TestCase(name: tc.name, project: project, person: person)
        testCaseService.save(test)
        to(EditTestCasePage, project.id, test.id)
        EditTestCasePage page = browser.page(EditTestCasePage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(st.name)
        page.edit()

        and:
        def show = at ShowTestCasePage
        show.goToEdit()

        when:
        page.scrollToBottom()
        page.stepsTable.removeBuilderRow(0)

        then:
        !page.stepsTable.isRemovedBuilderStepHiddenInputDisplayed()
    }
}
