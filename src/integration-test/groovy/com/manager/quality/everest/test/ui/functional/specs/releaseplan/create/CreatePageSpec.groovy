package com.manager.quality.everest.test.ui.functional.specs.releaseplan.create

import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.releaseplan.CreateReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePageSpec extends GebSpec {

    ProjectService projectService

    def setup() {
        given: "login as a basic user"
        to LoginPage
        def loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def projectId = projectService.list(max: 1).first().id
        to(CreateReleasePlanPage, projectId)
    }

    void "status options are correct"() {
        expect:
        def page = browser.page(CreateReleasePlanPage)
        page.statusSelectOptions*.text() == ['ToDo', 'Planning', 'In Progress', 'Released', 'Canceled']
    }

    void "name is required"() {
        given:
        def page = browser.page(CreateReleasePlanPage)
        page.nameInput = ''

        when:
        page.submit()

        then:
        at CreateReleasePlanPage
        page.errorsMessage.displayed
        page.errorsMessage.text() == 'Property [name] cannot be null'
    }
}
