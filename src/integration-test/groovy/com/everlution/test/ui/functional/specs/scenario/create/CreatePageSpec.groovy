package com.everlution.test.ui.functional.specs.scenario.create

import com.everlution.domains.Platform
import com.everlution.domains.Project
import com.everlution.services.project.ProjectService
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.scenario.CreateScenarioPage
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
    }

    void "verify platform options"() {
        given:
        def p = new Project(name: "verify platform options scenario", code: "vpos", platforms: [new Platform(name: "platform1 11")])
        def id = projectService.save(p).id

        when:
        def page = to (CreateScenarioPage, id)

        then: "correct options are populated"
        verifyAll {
            page.platformOptions*.text() == ["", "platform1 11"]
        }

        and: "default value is blank"
        verifyAll( {
            page.platformSelect().selected == ""
        })
    }
    void "verify method and type field options"() {
        setup:
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/scenario/create"

        expect: "correct options populate for executionMethod and type"
        CreateScenarioPage page = browser.page(CreateScenarioPage)
        verifyAll {
            page.executionMethodOptions*.text() == ["", "Automated", "Manual"]
            page.typeOptions*.text() == ["", "API", "UI"]
        }

        and: "default values are blank"
        verifyAll( {
            page.executionMethodSelect().selected == ""
            page.typeSelect().selected == ""
        })
    }
}
