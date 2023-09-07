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
        createPage.linkModule.searchInput = 's'
        createPage.linkModule.relationSelect().selected = 'Is Child of'
        createPage.linkModule.addButton.click()

        then:
        createPage.linkModule.validationMessage.displayed
    }

    void "validation message removed when link added"() {
        expect:
        false
    }

    void "data-id removed from search input once linked step added"() {
        expect:
        false
    }

    void "relation field reset when linked step added"() {
        expect:
        false
    }

    void "step name search field reset when linked step added"() {
        expect:
        false
    }

    void "linked steps display"() {
        expect:
        false
    }

    void "added linked step can be removed"() {
        expect:
        false
    }
}
