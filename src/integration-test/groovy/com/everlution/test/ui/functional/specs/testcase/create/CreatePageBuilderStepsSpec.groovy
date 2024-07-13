package com.everlution.test.ui.functional.specs.testcase.create

import com.everlution.domains.Link
import com.everlution.services.link.LinkService
import com.everlution.domains.Person
import com.everlution.services.person.PersonService
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.Relationship
import com.everlution.domains.StepTemplate
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class CreatePageBuilderStepsSpec extends GebSpec {

    LinkService linkService
    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService

    @Shared
    Person person

    @Shared
    Project project

    @Shared
    StepTemplate stepTemplate

    def setup() {
        given: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        project = projectService.list(max: 1).first()
        person = personService.list(max: 1).first()
        stepTemplate = stepTemplateService.findAllInProject(project, [max:1]).results.first()
        to(CreateTestCasePage, project.id)
    }

    void "suggestions only displayed for string of 3 characters or more"(int index) {
        given:
        def text = stepTemplate.name.substring(0, index)

        when:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.displaySearchStepsModal()
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
        def text = stepTemplate.name.substring(0, 3)

        when:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.displaySearchStepsModal()
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
        def text = stepTemplate.name.substring(0, 5)
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.submit()

        when:
        createPage.scrollToBottom()
        createPage.testStepTable.displaySearchStepsModal()
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
        createPage.testStepTable.addBuilderStep(stepTemplate.name)

        expect:
        createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed

        when:
        createPage.testStepTable.addBuilderStep(stepTemplate.name, false)

        then:
        !createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed
        createPage.testStepTable.getBuilderStep(1).find('input[value=Remove]').displayed
    }

    void "removing a step adds a remove link to previous step"() {
        given:
        CreateTestCasePage createPage = browser.page(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(stepTemplate.name)
        createPage.testStepTable.addBuilderStep(stepTemplate.name, false)

        expect:
        !createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed

        when:
        createPage.testStepTable.removeBuilderRow(1)

        then:
        createPage.testStepTable.getBuilderStep(0).find('input[value=Remove]').displayed
    }

    void "selecting suggested result displays properties and suggested steps"() {
        setup:
        def s = new StepTemplate(name: "test step", act: "action jackson", result: "result", project: project,
                person: person)
        stepTemplateService.save(s)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)

        then:
        createPage.testStepTable.isSuggestedStepDisplayed(stepTemplate.name)
        createPage.testStepTable.getCurrentBuilderStepName() == s.name
        createPage.testStepTable.isBuilderRowDisplayed(s.act, "", s.result)
    }

    void "selecting suggested step displays properties and suggested steps"() {
        setup:
        def s = new StepTemplate(name: "12345test544 step", act: "1action jackson345", result: "1result54",
                project: project, person: person)
        def st = new StepTemplate(name: "2445556test step 234535", act: "2action jackson 2454", result: "2result 254",
                project: project, person: person)
        stepTemplateService.save(s)
        stepTemplateService.save(st)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: stepTemplate.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)
        createPage.testStepTable.selectSuggestedStep(stepTemplate.name)

        then:
        createPage.testStepTable.isSuggestedStepDisplayed(st.name)
        createPage.testStepTable.getCurrentBuilderStepName() == stepTemplate.name
        createPage.testStepTable.isBuilderRowDisplayed(stepTemplate.act, "", stepTemplate.result)
    }

    void "suggested steps name and properties are repopulated with current step when step removed"() {
        given:
        def s = new StepTemplate(name: "3456666test step", act: "3action jackson", result: "3result", project: project,
                person: person)
        def st = new StepTemplate(name: "45678test step 2", act: "4action jackson 2", result: "4result 2", project: project,
                person: person)
        def stp = new StepTemplate(name: "45678te23442st step 2", act: "4ac23424tion jackson 2", result: "4res23424ult 2",
                project: project, person: person)
        stepTemplateService.save(s)
        stepTemplateService.save(st)
        stepTemplateService.save(stp)
        def l = new Link(ownerId: s.id, linkedId: stp.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        def li = new Link(ownerId: stp.id, linkedId: st.id, relation: Relationship.IS_SIBLING_OF.name, project: project)
        linkService.save(l)
        linkService.save(li)

        and:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)
        createPage.testStepTable.selectSuggestedStep(stp.name)

        expect:
        createPage.testStepTable.isSuggestedStepDisplayed(st.name)
        createPage.testStepTable.getCurrentBuilderStepName() == stp.name

        when:
        createPage.testStepTable.removeBuilderRow(1)

        then:
        createPage.testStepTable.isSuggestedStepDisplayed(stp.name)
        createPage.testStepTable.getCurrentBuilderStepName() == s.name
    }

    void "closing modal resets builder form"() {
        given:
        def s = new StepTemplate(name: "5test step", act: "5action jackson", result: "5result", project: project, person: person)
        stepTemplateService.save(s)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
        linkService.save(l)

        and:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)
        createPage.testStepTable.appendBuilderSteps()

        when:
        createPage.testStepTable.displaySearchStepsModal()

        then:
        createPage.testStepTable.getBuilderStepsCount() == 0
        !createPage.testStepTable.currentStepName.displayed
        createPage.testStepTable.getSuggestedStepsCount() == 0
    }

    void "form is reset when last step is removed"() {
        given:
        def s = new StepTemplate(name: "6test step", act: "6action jackson", result: "6result", project: project, person: person)
        stepTemplateService.save(s)
        def l = new Link(ownerId: s.id, linkedId: stepTemplate.id, relation: Relationship.IS_PARENT_OF.name, project: project)
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

    void "no results message displayed when no related steps found"() {
        setup:
        def s = new StepTemplate(name: "7test step", act: "7action jackson", result: "7result", project: project, person: person)
        stepTemplateService.save(s)

        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(s.name)

        then:
        createPage.testStepTable.noSuggestedStepsText.displayed
    }

    void "builder step row has hidden input"() {
        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.addBuilderStep(stepTemplate.name)

        then:
        !createPage.testStepTable.isBuilderStepHiddenInputDisplayed(0)
    }

    void "search results are correctly displayed"(int start, int end) {
        given:
        def text = stepTemplate.name.substring(start, end)

        when:
        def createPage = createPage(CreateTestCasePage)
        createPage.scrollToBottom()
        createPage.testStepTable.displaySearchStepsModal()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.testStepTable.searchInput << s
        }
        waitFor {
            createPage.testStepTable.searchResultsMenu.displayed
        }

        then:
        createPage.testStepTable.searchResults.first().text() == stepTemplate.name

        where:
        start | end
        0     | 7
        5     | 10
    }
}
