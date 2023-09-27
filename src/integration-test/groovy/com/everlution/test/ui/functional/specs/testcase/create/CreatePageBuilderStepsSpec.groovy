package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.Link
import com.everlution.LinkService
import com.everlution.Person
import com.everlution.PersonService
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Relationship
import com.everlution.Step
import com.everlution.StepService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreatePageBuilderStepsSpec extends GebSpec {

    LinkService linkService
    PersonService personService
    ProjectService projectService
    StepService stepService

    @Shared
    Person person

    @Shared
    Project project

    @Shared
    Step step

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        project = projectService.list(max: 1).first()
        person = personService.list(max: 1).first()
        step = stepService.findAllByProject(project, [max:1]).results.first()
        to(CreateTestCasePage, project.id)
    }

    void "suggestions only displayed for string of 3 characters or more"(int index) {
        given:
        def text = step.name.substring(0, index)

        when:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.testStepTable.searchInput << s
        }
        sleep(500)

        then:
        !createPage.testStepTable.searchResultsMenu.displayed

        where:
        index << [1,2]
    }

    void "suggestions display for string of 3 characters or more"() {
        given:
        def text = step.name.substring(0, 3)

        when:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.testStepTable.searchInput << s
        }
        sleep(500)

        then:
        createPage.testStepTable.searchResultsMenu.displayed
    }

    void "steps are retrieved when validation fails"() {
        given:
        def text = step.name.substring(0, 5)
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.submit()

        when:
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.testStepTable.searchInput << s
        }
        sleep(500)

        then:
        createPage.testStepTable.searchResultsMenu.displayed
    }

    void "adding a step removes the remove link from previous step"() {
        given:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(step.name)

        expect:
        createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed

        when:
        createPage.testStepTable.addBuilderStep(step.name)

        then:
        !createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed
        createPage.testStepTable.getBuilderStep(1).find('input[value=Remove]').displayed
    }

    void "removing a step adds a remove link to previous step"() {
        given:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(step.name)
        createPage.testStepTable.addBuilderStep(step.name)

        expect:
        !createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed

        when:
        createPage.testStepTable.removeBuilderRow(1)

        then:
        createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed
    }

    void "selecting suggested result displays properties and suggested steps"() {
        setup:
        def s = new Step(name: "test step", act: "action jackson", result: "result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)

        then:
        createPage.testStepTable.isSuggestedStepDisplayed(step.name)
        createPage.testStepTable.getCurrentBuilderStepName() == s.name
        createPage.testStepTable.isBuilderRowDisplayed(s.act, s.result)
    }

    void "selecting suggested step displays properties and suggested steps"() {
        setup:
        def s = new Step(name: "1test step", act: "1action jackson", result: "1result", project: project, person: person,
                isBuilderStep: true)
        def st = new Step(name: "2test step 2", act: "2action jackson 2", result: "2result 2", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        stepService.save(st)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: step.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)
        createPage.testStepTable.selectSuggestedStep(step.name)

        then:
        createPage.testStepTable.isSuggestedStepDisplayed(st.name)
        createPage.testStepTable.getCurrentBuilderStepName() == step.name
        createPage.testStepTable.isBuilderRowDisplayed(step.act, step.result)
    }

    void "suggested steps name and properties are repopulated with current step when step removed"() {
        given:
        def s = new Step(name: "3test step", act: "3action jackson", result: "3result", project: project, person: person,
                isBuilderStep: true)
        def st = new Step(name: "4test step 2", act: "4action jackson 2", result: "4result 2", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        stepService.save(st)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: step.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        and:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)
        createPage.testStepTable.selectSuggestedStep(step.name)

        expect:
        createPage.testStepTable.isSuggestedStepDisplayed(st.name)
        createPage.testStepTable.getCurrentBuilderStepName() == step.name

        when:
        createPage.testStepTable.removeBuilderRow(1)

        then:
        createPage.testStepTable.isSuggestedStepDisplayed(step.name)
        createPage.testStepTable.getCurrentBuilderStepName() == s.name
    }

    void "changing step type resets builder form"() {
        given:
        def s = new Step(name: "5test step", act: "5action jackson", result: "5result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)

        expect:
        createPage.testStepTable.getBuilderStepsCount() == 1
        createPage.testStepTable.currentStepName.displayed
        createPage.testStepTable.getSuggestedStepsCount() == 1

        when:
        createPage.testStepTable.selectStepsTab('free-form')
        createPage.testStepTable.selectStepsTab('builder')

        then:
        createPage.testStepTable.getBuilderStepsCount() == 0
        !createPage.testStepTable.currentStepName.displayed
        createPage.testStepTable.getSuggestedStepsCount() == 0
    }

    void "form is reset when last step is removed"() {
        given:
        def s = new Step(name: "6test step", act: "6action jackson", result: "6result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)

        expect:
        createPage.testStepTable.getBuilderStepsCount() == 1
        createPage.testStepTable.currentStepName.displayed
        createPage.testStepTable.getSuggestedStepsCount() == 1

        when:
        createPage.testStepTable.removeBuilderRow(0)

        then:
        createPage.testStepTable.getBuilderStepsCount() == 0
        !createPage.testStepTable.currentStepName.displayed
        createPage.testStepTable.getSuggestedStepsCount() == 0
    }

    void "suggestion results are removed when clicked outside of menu"() {
        given:
        def text = step.name.substring(0, 3)
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.testStepTable.searchInput << s
        }
        sleep(500)

        expect:
        createPage.testStepTable.searchResultsMenu.displayed

        when:
        createPage.testStepTable.builderTab.click()

        then:
        !createPage.testStepTable.searchResultsMenu.displayed
    }

    void "no results message displayed when no related steps found"() {
        setup:
        def s = new Step(name: "7test step", act: "7action jackson", result: "7result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)

        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)

        then:
        createPage.testStepTable.noSuggestedStepsText.displayed
    }
}
