package com.everlution.test.ui.functional.specs.step.edit

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.step.EditStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@Integration
class EditStepPageSpec extends GebSpec {

    ProjectService projectService
    StepService stepService

    @Shared
    Project project

    @Shared
    Step step

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        project = projectService.list(max: 1).first()
        step = stepService.findAllByProject(project, [max:1]).results.first()
        to (EditStepPage, project.id, step.id)
    }

    void "error message displays on view"() {
        when:
        def editPage = browser.at(EditStepPage)
        editPage.editStep("action", "", "result")

        then:
        editPage.errorsMessage.displayed
    }

    void "error message displays when action and result are blank"() {
        when: "create"
        def page = browser.page(EditStepPage)
        page.editStep("", "test", "")

        then:
        page.errorsMessage.size() == 2
    }

    void "steps are retrieved when validation fails"() {
        setup:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()
        def page = browser.page(EditStepPage)
        page.editStep("test", "", "")

        when:
        page.scrollToBottom()
        page.linkModule.addLink(step.name, 'Is Child of')

        then:
        page.linkModule.isLinkDisplayed(step.name)
    }

    void "tooltips display when fields blank"(String name, String relation, String tipText) {
        when:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.linkModule.searchInput = name
        editPage.linkModule.relationSelect().selected = relation
        editPage.linkModule.addButton.click()

        then:
        editPage.linkModule.getToolTipText() == tipText

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
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        for (int i = 0; i < text.length(); i++){
            char c = text.charAt(i)
            String s = new StringBuilder().append(c).toString()
            editPage.linkModule.searchInput << s
        }
        sleep(500)

        then:
        !editPage.linkModule.searchResultsMenu.displayed

        where:
        index << [1,2,3]
    }

    void "validation message displayed when name not selected from list"() {
        when:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.linkModule.searchInput << 's'
        editPage.linkModule.relationSelect().selected = 'Is Child of'
        editPage.linkModule.addButton.click()

        then:
        editPage.linkModule.validationMessage.displayed
    }

    void "validation message removed when link added"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.linkModule.searchInput << 's'
        editPage.linkModule.relationSelect().selected = 'Is Child of'
        editPage.linkModule.addButton.click()

        expect:
        editPage.linkModule.validationMessage.displayed

        when:
        editPage.scrollToBottom()
        editPage.linkModule.addLink(step.name, 'Is Parent of')

        then:
        !editPage.linkModule.validationMessage.displayed
    }

    void "data-id removed from search input once linked step added"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.setLinkProperties(step.name, 'Is Parent of')

        expect:
        editPage.linkModule.searchInput.attr('data-id') as Long == step.id

        when:
        editPage.linkModule.addLink(step.name, 'Is Parent of')

        then:
        !editPage.linkModule.searchInput.attr('data-id')
    }

    void "hidden inputs present for linked items"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        when:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(step.name, 'Is Parent of')

        then:
        def id = editPage.linkModule.getLinkedItemHiddenInput(step.name, 'id')
        def relation = editPage.linkModule.getLinkedItemHiddenInput(step.name, 'relation')
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
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.setLinkProperties(step.name, 'Is Parent of')

        expect:
        editPage.linkModule.relationSelect().selected == 'Is Parent of'
        editPage.linkModule.searchInput.value() == step.name

        when:
        editPage.linkModule.addButton.click()

        then:
        editPage.linkModule.relationSelect().selected == ''
        editPage.linkModule.searchInput.text() == ''
    }

    void "added linked step can be removed"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(step.name, 'Is Parent of')

        expect:
        editPage.linkModule.isLinkDisplayed(step.name)

        when:
        editPage.linkModule.removeLinkedItem(0)

        then:
        !editPage.linkModule.isLinkDisplayed(step.name)
    }

    void "correct relation field options are present"() {
        expect:
        def page = browser.page(EditStepPage)
        List found = page.linkModule.relationOptions*.text()
        def expected = ["", "Is Child of", "Is Sibling of", "Is Parent of"]
        found.size() == expected.size()
        expected.containsAll(found)
    }

    void "linked step is placed in correct row"(String id, String relation) {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        when:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(step.name, relation)

        then:
        $("#${id}").find('[data-test-id=linkedItemName]').text().endsWith(step.name)

        where:
        id        | relation
        'parents' | 'Is Child of'
        'siblings'| 'Is Sibling of'
        'children'| 'Is Parent of'
    }

    void "removed items hidden input added when link removed"() {
        given:
        Step step = stepService.findAllByProject(project, [max:1]).results.first()

        and:
        EditStepPage editPage = browser.page(EditStepPage)
        editPage.scrollToBottom()
        editPage.linkModule.addLink(step.name, 'Is Parent of')
        editPage.edit()

        expect:
        def s = browser.page(ShowStepPage)
        s.isLinkDisplayed(step.name, 'children')

        when:
        s.goToEdit()
        editPage.scrollToBottom()
        editPage.linkModule.removeLinkedItem(0)

        then:
        def ele = $('[data-test-id=removed-tag-input]')
        ele.value() == step.id.toString()
        !ele.displayed
    }
}