package com.everlution.test.ui.functional.specs.bug.edit

import com.everlution.*
import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.EditBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditPageBuilderStepsSpec extends GebSpec {

    BugService bugService
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
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        project = projectService.list(max: 1).first()
        person = personService.list(max: 1).first()
        step = stepService.findAllByProject(project, [max:1]).results.first()
        def bd = DataFactory.bug()
        def bug = new Bug(name: bd.name, status: 'Open', project: project, person: person)
        bugService.save(bug)
        to(EditBugPage, project.id, bug.id)
    }

    void "suggestions only displayed for string of 3 characters or more"(int index) {
        given:
        def text = step.name.substring(0, index)

        when:
        EditBugPage page = browser.page(EditBugPage)
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
        def text = step.name.substring(0, 3)

        when:
        EditBugPage page = browser.page(EditBugPage)
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
        def text = step.name.substring(0, 5)
        EditBugPage page = browser.page(EditBugPage)
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
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(step.name)
        page.stepsTable.addBuilderStep(step.name)

        then:
        !page.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed
        page.stepsTable.getBuilderStep(1).find('input[value=Remove]').displayed
    }

    void "removing a step adds a remove link to previous step"() {
        given:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(step.name)
        page.stepsTable.addBuilderStep(step.name)

        when:
        page.stepsTable.removeBuilderRow(1)

        then:
        page.stepsTable.getBuilderStep(0).find('input[value=Remove]').displayed
    }

    void "selecting suggested result displays properties and suggested steps"() {
        setup:
        def s = new Step(name: "test step5345", act: "action jackson", result: "result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        when:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)

        then:
        page.stepsTable.isSuggestedStepDisplayed(step.name)
        page.stepsTable.getCurrentBuilderStepName() == s.name
        page.stepsTable.isBuilderRowDisplayed(s.act, s.result)
    }

    void "selecting suggested step displays properties and suggested steps"() {
        setup:
        def s = new Step(name: "1test step435435", act: "1action jackson", result: "1result", project: project, person: person,
                isBuilderStep: true)
        def st = new Step(name: "2test step 232342", act: "2action jackson 2", result: "2result 2", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        stepService.save(st)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: step.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        when:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)
        page.stepsTable.selectSuggestedStep(step.name)

        then:
        page.stepsTable.isSuggestedStepDisplayed(st.name)
        page.stepsTable.getCurrentBuilderStepName() == step.name
        page.stepsTable.isBuilderRowDisplayed(step.act, step.result)
    }

    void "suggested steps name and properties are repopulated with current step when step removed"() {
        given:
        def s = new Step(name: "3test step2312424", act: "3action jackson", result: "3result", project: project, person: person,
                isBuilderStep: true)
        def st = new Step(name: "4test step 224312313213", act: "4action jackson 2", result: "4result 2", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        stepService.save(st)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: step.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        and:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)
        page.stepsTable.selectSuggestedStep(step.name)

        expect:
        page.stepsTable.isSuggestedStepDisplayed(st.name)
        page.stepsTable.getCurrentBuilderStepName() == step.name

        when:
        page.stepsTable.removeBuilderRow(1)

        then:
        page.stepsTable.isSuggestedStepDisplayed(step.name)
        page.stepsTable.getCurrentBuilderStepName() == s.name
    }

    void "suggested steps and current step name are displayed upon page load"() {
        given:
        def s = new Step(name: "33test 12313step", act: "33action jackson", result: "33result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)
        page.edit()

        when:
        def show = at ShowBugPage
        show.goToEdit()

        then:
        page.stepsTable.isSuggestedStepDisplayed(step.name)
        page.stepsTable.getCurrentBuilderStepName() == s.name
    }

    void "form is reset when last step is removed"() {
        given:
        def s = new Step(name: "6test ste3242424p", act: "6action jackson", result: "6result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)
        def l = new Link(ownerId: s.id, linkedId: step.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        EditBugPage page = browser.page(EditBugPage)
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
        def s = new Step(name: "7test step23455", act: "7action jackson", result: "7result", project: project, person: person,
                isBuilderStep: true)
        stepService.save(s)

        when:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(s.name)

        then:
        page.stepsTable.noSuggestedStepsText.displayed
    }

    void "builder step row has hidden input"() {
        when:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(step.name)

        then:
        !page.stepsTable.isBuilderStepHiddenInputDisplayed(0)
    }

    void "removing step adds hidden input"() {
        given:
        EditBugPage page = browser.page(EditBugPage)
        page.scrollToBottom()
        page.stepsTable.addBuilderStep(step.name)
        page.edit()

        and:
        def show = at ShowBugPage
        show.goToEdit()

        when:
        page.scrollToBottom()
        page.stepsTable.removeBuilderRow(0)

        then:
        !page.stepsTable.isRemovedBuilderStepHiddenInputDisplayed()
    }
}