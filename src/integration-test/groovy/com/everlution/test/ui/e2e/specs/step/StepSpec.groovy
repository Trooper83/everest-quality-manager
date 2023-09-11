package com.everlution.test.ui.e2e.specs.step

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.step.CreateStepPage
import com.everlution.test.ui.support.pages.step.EditStepPage
import com.everlution.test.ui.support.pages.step.ListStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
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
        projectHomePage.sideBar.goToCreate("Step")

        CreateStepPage page = browser.at(CreateStepPage)
        page.createStep(step.action, step.name, step.result)
    }

    void "step can be created"() {
        expect:
        def show = at ShowStepPage
        verifyAll {
            show.nameValue.text() == step.name
            show.actionValue.text() == step.action
            show.resultValue.text() == step.result
        }
    }

    void "links created with step"() {
        given:
        def show = at ShowStepPage
        show.sideBar.goToCreate('Step')
        def st = DataFactory.step()

        when:
        def page = at CreateStepPage
        page.createStepWithLink(st.action, st.name, st.result, step.name, 'Is Child of')

        then:
        def sPage = at ShowStepPage
        sPage.isLinkDisplayed(step.name, 'parents')
    }

    void "step can be edited"() {
        given:
        def s = DataFactory.step()
        def show = at ShowStepPage
        show.goToEdit()

        when:
        browser.at(EditStepPage).editStep(s.action, s.name, s.result)

        then:
        def page = at ShowStepPage
        verifyAll {
            page.nameValue.text() == s.name
            page.actionValue.text() == s.action
            page.resultValue.text() == s.result
        }
    }

    void "links can be added and removed"() {
        expect:
        false
    }

    void "step can be deleted"() {
        when:
        browser.at(ShowStepPage).delete()

        then:
        def page = at ListStepPage
        page.statusMessage.displayed
    }

    void "step with links can be deleted"() {
        given:
        def show = at ShowStepPage
        show.sideBar.goToCreate('Step')
        def st = DataFactory.step()
        def page = at CreateStepPage
        page.createStepWithLink(st.action, st.name, st.result, step.name, 'Is Child of')

        when:
        def sPage = at ShowStepPage
        sPage.delete()

        then:
        def lPage = at ListStepPage
        lPage.statusMessage.displayed
    }
}
