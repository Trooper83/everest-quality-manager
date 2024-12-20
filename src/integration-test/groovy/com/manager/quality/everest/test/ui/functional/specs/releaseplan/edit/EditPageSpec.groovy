package com.manager.quality.everest.test.ui.functional.specs.releaseplan.edit

import com.manager.quality.everest.services.person.PersonService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.EditReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class EditPageSpec extends GebSpec {

    PersonService personService

    def setup() {
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)
        def person = personService.list(max:1).first()
        def plan = DataFactory.createReleasePlan(person)
        to (EditReleasePlanPage, plan.project.id, plan.id)
    }

    void "name is required"() {
        when:
        def page = browser.page(EditReleasePlanPage)
        page.nameInput = ''
        page.edit()

        then:
        at EditReleasePlanPage
        page.errorsMessage.displayed
        page.errorsMessage.text() == 'Property [name] cannot be null'
    }

    void "status options are correct"() {
        expect:
        def page = browser.page(EditReleasePlanPage)
        page.statusSelectOptions*.text() == ['ToDo', 'Planning', 'In Progress', 'Released', 'Canceled']
    }
}
