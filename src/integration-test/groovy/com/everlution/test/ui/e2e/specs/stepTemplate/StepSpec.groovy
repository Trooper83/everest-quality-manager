package com.everlution.test.ui.e2e.specs.stepTemplate

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.bug.CreateBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.stepTemplate.CreateStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.EditStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ListStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
import com.everlution.test.ui.support.pages.testcase.CreateTestCasePage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec

class StepSpec extends GebSpec {

    def step = DataFactory.step()

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 'Atlas')

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Step Template")

        def page = browser.at(CreateStepTemplatePage)
        page.createStepTemplate(step.action, step.name, step.result)
    }

    void "step can be created"() {
        expect:
        def show = at ShowStepTemplatePage
        verifyAll {
            show.nameValue.text() == step.name
            show.actionValue.text() == step.action
            show.resultValue.text() == step.result
        }
    }

    void "links created with step"() {
        given:
        def show = at ShowStepTemplatePage
        show.sideBar.goToCreate('Step Template')
        def st = DataFactory.step()

        when:
        def page = at CreateStepTemplatePage
        page.createStepTemplateWithLink(st.action, st.name, st.result, step.name, 'Is Child of')

        then:
        def sPage = at ShowStepTemplatePage
        sPage.isLinkDisplayed(step.name, 'parents')
    }

    void "step can be edited"() {
        given:
        def s = DataFactory.step()
        def show = at ShowStepTemplatePage
        show.goToEdit()

        when:
        browser.at(EditStepTemplatePage).editStepTemplate(s.action, s.name, s.result)

        then:
        def page = at ShowStepTemplatePage
        verifyAll {
            page.nameValue.text() == s.name
            page.actionValue.text() == s.action
            page.resultValue.text() == s.result
        }
    }

    void "links can be added and removed"() {
        given:
        def show = at ShowStepTemplatePage
        show.sideBar.goToCreate('Step Template')
        def st = DataFactory.step()
        def page = at CreateStepTemplatePage
        page.createStepTemplateWithLink(st.action, st.name, st.result, step.name, 'Is Child of')

        expect:
        def sPage = at ShowStepTemplatePage
        sPage.isLinkDisplayed(step.name, 'parents')
        sPage.goToEdit()

        when:
        def edit = browser.at(EditStepTemplatePage)
        edit.scrollToBottom()
        edit.linkModule.removeLinkedItem(0)
        edit.edit()

        then:
        !sPage.isLinkDisplayed(step.name, 'parents')
    }

    void "step can be deleted"() {
        when:
        browser.at(ShowStepTemplatePage).delete()

        then:
        def page = at ListStepTemplatePage
        page.statusMessage.displayed
    }

    void "step with links can be deleted"() {
        given:
        def show = at ShowStepTemplatePage
        show.sideBar.goToCreate('Step Template')
        def st = DataFactory.step()
        def page = at CreateStepTemplatePage
        page.createStepTemplateWithLink(st.action, st.name, st.result, step.name, 'Is Child of')

        when:
        def sPage = at ShowStepTemplatePage
        sPage.delete()

        then:
        def lPage = at ListStepTemplatePage
        lPage.statusMessage.displayed
    }

    void "deleting test with related builder step does not delete step"() {
        given:
        def page = at ShowStepTemplatePage
        def url = currentUrl
        page.sideBar.goToCreate('Test Case')
        def create = browser.page(CreateTestCasePage)
        create.createBuilderTestCase('E2E test delete test case with step', '', '', [], [],
                '', '', '', [step.name])
        browser.page(ShowTestCasePage).delete()

        expect:
        at ListTestCasePage

        when:
        go(url)

        then:
        def showPage = at ShowStepTemplatePage
        showPage.nameValue.text() == step.name
    }
}
