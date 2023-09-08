package com.everlution.test.ui.functional.specs.step.create

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.step.CreateStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class CreateStepPageSpec extends GebSpec {

    ProjectService projectService
    StepService stepService

    @Shared
    Project project

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        project = projectService.list(max: 1).first()
        to (CreateStepPage, project.id)
    }

    void "error message displays when name blank"() {
        when: "create"
        def createPage = browser.page(CreateStepPage)
        createPage.createStep("action", "", "result")

        then:
        createPage.errorsMessage.displayed
    }

    void "error message displays when action and result are blank"() {
        when: "create"
        def createPage = browser.page(CreateStepPage)
        createPage.createStep("", "test", "")

        then:
        createPage.errorsMessage.size() == 2
    }

    void "steps are retrieved when validation fails"() {
        setup:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()
        def createPage = browser.page(CreateStepPage)
        createPage.createStep("test", "", "")

        when:
        createPage.scrollToBottom()
        createPage.linkModule.addLink(step.name, 'Is Child of')

        then:
        createPage.linkModule.isLinkDisplayed(step.name, 'Is Child of')
    }

    void "tooltips display when they should"(String name, String relation, String tipText) {
        when:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.linkModule.searchInput = name
        createPage.linkModule.relationSelect().selected = relation
        createPage.linkModule.addButton.click()

        then:
        createPage.linkModule.getToolTipText() == tipText

        where:
        name     | relation      | tipText
        ''       | 'Is Child of' | 'Field cannot be blank'
        'name'   | ''            | 'Field cannot be blank'
    }

    void "results fetched only when three characters typed"(int index) {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()
        def text = step.name.substring(0, index)

        when:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            createPage.linkModule.searchInput << s
        }
        sleep(500)

        then:
        !createPage.linkModule.searchResultsMenu.displayed

        where:
        index << [1,2,3]
    }

    void "validation message displayed when name not selected from list"() {
        when:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.linkModule.searchInput << 's'
        createPage.linkModule.relationSelect().selected = 'Is Child of'
        createPage.linkModule.addButton.click()

        then:
        createPage.linkModule.validationMessage.displayed
    }

    void "validation message removed when link added"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.linkModule.searchInput << 's'
        createPage.linkModule.relationSelect().selected = 'Is Child of'
        createPage.linkModule.addButton.click()

        expect:
        createPage.linkModule.validationMessage.displayed

        when:
        createPage.scrollToBottom()
        createPage.linkModule.addLink(step.name, 'Is Parent of')

        then:
        !createPage.linkModule.validationMessage.displayed
    }

    void "data-id removed from search input once linked step added"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.scrollToBottom()
        createPage.linkModule.setLinkProperties(step.name, 'Is Parent of')

        expect:
        createPage.linkModule.searchInput.attr('data-id') as Long == step.id

        when:
        createPage.linkModule.addLink(step.name, 'Is Parent of')

        then:
        !createPage.linkModule.searchInput.attr('data-id')
    }

    void "hidden inputs present for linked items"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        when:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.scrollToBottom()
        createPage.linkModule.addLink(step.name, 'Is Parent of')

        then:
        def id = createPage.linkModule.getLinkedItemHiddenInput(0, 'id')
        def relation = createPage.linkModule.getLinkedItemHiddenInput(0, 'relation')
        verifyAll {
            id.value() as Long == step.id
            !id.displayed
            relation.value() == 'Is Parent of'
            !relation.displayed
        }
    }

    void "link fields reset when linked step added"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.scrollToBottom()
        createPage.linkModule.setLinkProperties(step.name, 'Is Parent of')

        expect:
        createPage.linkModule.relationSelect().selected == 'Is Parent of'
        createPage.linkModule.searchInput.value() == step.name

        when:
        createPage.linkModule.addButton.click()

        then:
        createPage.linkModule.relationSelect().selected == ''
        createPage.linkModule.searchInput.text() == ''
    }

    void "added linked step can be removed"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        CreateStepPage createPage = browser.page(CreateStepPage)
        createPage.scrollToBottom()
        createPage.linkModule.addLink(step.name, 'Is Parent of')

        expect:
        createPage.linkModule.isLinkDisplayed(step.name, 'Is Parent of')

        when:
        createPage.linkModule.removeLinkedItem(0)

        then:
        !createPage.linkModule.isLinkDisplayed(step.name, 'Is Parent of')
    }
}
