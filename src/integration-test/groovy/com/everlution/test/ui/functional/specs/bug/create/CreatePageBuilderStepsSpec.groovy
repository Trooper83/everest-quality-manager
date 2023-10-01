package com.everlution.test.ui.functional.specs.bug.create

import com.everlution.*
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
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
        to(CreateBugPage, project.id)
    }

    void "suggestions only displayed for string of 3 characters or more"(int index) {
        given:
        def text = step.name.substring(0, index)

        when:
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.stepsTable.searchInput << s
        }
        sleep(500)

        then:
        !createPage.stepsTable.searchResultsMenu.displayed

        where:
        index << [1,2]
    }

    void "suggestions display for string of 3 characters or more"() {
        given:
        def text = step.name.substring(0, 3)

        when:
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.stepsTable.searchInput << s
        }
        sleep(500)

        then:
        createPage.stepsTable.searchResultsMenu.displayed
    }

    void "steps are retrieved when validation fails"() {
        given:
        def text = step.name.substring(0, 5)
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.submit()

        when:
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.stepsTable.searchInput << s
        }
        sleep(500)

        then:
        createPage.stepsTable.searchResultsMenu.displayed
    }

    void "adding a step removes the remove link from previous step"() {
        given:
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(step.name)

        expect:
        createPage.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed

        when:
        createPage.stepsTable.addBuilderStep(step.name)

        then:
        !createPage.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed
        createPage.stepsTable.getBuilderStep(1).find('input[value=Remove]').displayed
    }

    void "removing a step adds a remove link to previous step"() {
        given:
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(step.name)
        createPage.stepsTable.addBuilderStep(step.name)

        expect:
        !createPage.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed

        when:
        createPage.stepsTable.removeBuilderRow(1)

        then:
        createPage.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed
    }

    void "selecting suggested result displays properties and suggested steps"() {
        setup:
        def s = new Step(name: "test step", act: "action jackson", result: "result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        when:
        def createPage = createPage(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(s.name)

        then:
        createPage.stepsTable.isSuggestedStepDisplayed(step.name)
        createPage.stepsTable.getCurrentBuilderStepName() == s.name
        createPage.stepsTable.isBuilderRowDisplayed(s.act, s.result)
    }

    void "selecting suggested step displays properties and suggested steps"() {
        setup:
        def s = new Step(name: "12345test step", act: "1action jackson", result: "1result", project: project, person: person,
                isBuilderStep: true)
        def st = new Step(name: "2445556test step 2", act: "2action jackson 2", result: "2result 2", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        stepService.save(st)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: step.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        when:
        def createPage = createPage(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(s.name)
        createPage.stepsTable.selectSuggestedStep(step.name)

        then:
        createPage.stepsTable.isSuggestedStepDisplayed(st.name)
        createPage.stepsTable.getCurrentBuilderStepName() == step.name
        createPage.stepsTable.isBuilderRowDisplayed(step.act, step.result)
    }

    void "suggested steps name and properties are repopulated with current step when step removed"() {
        given:
        def s = new Step(name: "3456666test step", act: "3action jackson", result: "3result", project: project, person: person,
                isBuilderStep: true)
        def st = new Step(name: "45678test step 2", act: "4action jackson 2", result: "4result 2", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        stepService.save(st)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: step.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        and:
        def createPage = createPage(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(s.name)
        createPage.stepsTable.selectSuggestedStep(step.name)

        expect:
        createPage.stepsTable.isSuggestedStepDisplayed(st.name)
        createPage.stepsTable.getCurrentBuilderStepName() == step.name

        when:
        createPage.stepsTable.removeBuilderRow(1)

        then:
        createPage.stepsTable.isSuggestedStepDisplayed(step.name)
        createPage.stepsTable.getCurrentBuilderStepName() == s.name
    }

    void "changing step type resets builder form"() {
        given:
        def s = new Step(name: "5test step", act: "5action jackson", result: "5result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        def createPage = createPage(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(s.name)

        expect:
        createPage.stepsTable.getBuilderStepsCount() == 1
        createPage.stepsTable.currentStepName.displayed
        createPage.stepsTable.getSuggestedStepsCount() == 1

        when:
        createPage.stepsTable.selectStepsTab('free-form')
        createPage.stepsTable.selectStepsTab('builder')

        then:
        createPage.stepsTable.getBuilderStepsCount() == 0
        !createPage.stepsTable.currentStepName.displayed
        createPage.stepsTable.getSuggestedStepsCount() == 0
    }

    void "form is reset when last step is removed"() {
        given:
        def s = new Step(name: "6test step", act: "6action jackson", result: "6result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        def createPage = createPage(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(s.name)

        expect:
        createPage.stepsTable.getBuilderStepsCount() == 1
        createPage.stepsTable.currentStepName.displayed
        createPage.stepsTable.getSuggestedStepsCount() == 1

        when:
        createPage.stepsTable.removeBuilderRow(0)

        then:
        createPage.stepsTable.getBuilderStepsCount() == 0
        !createPage.stepsTable.currentStepName.displayed
        createPage.stepsTable.getSuggestedStepsCount() == 0
    }

    void "suggestion results are removed when clicked outside of menu"() {
        given:
        def text = step.name.substring(0, 3)
        CreateBugPage createPage = browser.page(CreateBugPage)
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.stepsTable.searchInput << s
        }
        sleep(500)

        expect:
        createPage.stepsTable.searchResultsMenu.displayed

        when:
        createPage.stepsTable.builderTab.click()

        then:
        !createPage.stepsTable.searchResultsMenu.displayed
    }

    void "no results message displayed when no related steps found"() {
        setup:
        def s = new Step(name: "7test step", act: "7action jackson", result: "7result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)

        when:
        def createPage = createPage(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(s.name)

        then:
        createPage.stepsTable.noSuggestedStepsText.displayed
    }

    void "builder step row has hidden input"() {
        when:
        def createPage = createPage(CreateBugPage)
        createPage.scrollToBottom()
        createPage.stepsTable.addBuilderStep(step.name)

        then:
        !createPage.stepsTable.isBuilderStepHiddenInputDisplayed(0)
    }
}
