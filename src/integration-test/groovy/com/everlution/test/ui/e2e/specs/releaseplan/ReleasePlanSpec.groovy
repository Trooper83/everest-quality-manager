package com.everlution.test.ui.e2e.specs.releaseplan

import com.everlution.test.support.DataFactory
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.ListProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.EditReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ListReleasePlanPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import com.everlution.test.ui.support.pages.testcycle.ShowTestCyclePage
import com.everlution.test.ui.support.pages.testiteration.ExecuteTestIterationPage
import com.everlution.test.ui.support.pages.testiteration.ShowTestIterationPage
import geb.spock.GebSpec

class ReleasePlanSpec extends GebSpec {

    String futureDate
    String name
    String pastDate

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        def projectsPage = at(ListProjectPage)
        projectsPage.projectTable.clickCell('Name', 'Atlas')

        def projectHomePage = at ProjectHomePage
        projectHomePage.sideBar.goToCreate("Release Plan")

        name = DataFactory.releasePlan().name
        futureDate = DataFactory.getFutureDate(10)
        pastDate = DataFactory.getPastDate(0)
        browser.page(CreateReleasePlanPage)
                .createReleasePlan(name, "ToDo", pastDate, futureDate)
    }

    void "release plan is created and data persists"() {
        expect: "at show page"
        def show = at ShowReleasePlanPage
        verifyAll {
            show.nameValue.text() == name
            show.statusValue.text() == "ToDo"
            show.plannedDateValue.text() == pastDate
            show.releaseDateValue.text() == futureDate
        }
    }

    void "test cycle can be added to plan"() {
        when:
        def show = browser.page(ShowReleasePlanPage)
        show.createTestCycle("Test cycle", "Integrated", "Web")

        then:
        show.isTestCyclePresent("Test cycle")
    }

    void "test iteration result persists"() {
        given:
        def show = browser.page(ShowReleasePlanPage)
        show.createTestCycle("Test cycle", "Integrated", "Web")
        show.goToTestCycle(0)

        and:
        def cycle = at ShowTestCyclePage
        cycle.addTestsByGroup()

        and:
        cycle.testsTable.clickCell("", 0)

        when:
        def page = browser.page(ExecuteTestIterationPage)
        page.setResult("Pass", "Some notes")

        then:
        def iteration = at ShowTestIterationPage
        iteration.resultValue.text() == "Pass"
        iteration.notesValue.text() == "Some notes"
    }

    void "release plan can be edited"() {
        given:
        browser.page(ShowReleasePlanPage).goToEdit()

        when:
        def future = DataFactory.getFutureDate(11)
        def past = DataFactory.getPastDate(1)
        browser.page(EditReleasePlanPage)
                .editReleasePlan(name + ' edited', 'Planning', past, future)

        then:
        def showPage = at ShowReleasePlanPage
        verifyAll {
            showPage.nameValue.text() == name + ' edited'
            showPage.statusValue.text() == "Planning"
            showPage.plannedDateValue.text() == past
            showPage.releaseDateValue.text() == future
        }
    }

    void "release plan can be deleted"() {
        when:
        browser.page(ShowReleasePlanPage).deletePlan()

        then:
        def list = at ListReleasePlanPage
        !list.listTable.isValueInColumn('Name', name)
    }
}
