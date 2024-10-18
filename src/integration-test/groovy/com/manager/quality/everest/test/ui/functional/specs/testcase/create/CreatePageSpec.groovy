package com.manager.quality.everest.test.ui.functional.specs.testcase.create

import com.manager.quality.everest.domains.Platform
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testcase.CreateTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePageSpec extends GebSpec {

    ProjectService projectService

    void "verify method and type field options"() {
        setup: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and: "go to the create page"
        def project = projectService.list(max: 1).first()
        go "/project/${project.id}/testCase/create"

        expect: "correct options populate for executionMethod and type"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
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

    void "verify platform options"() {
        given:
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        and:
        def pl = new Platform(name: "Testing 123")
        def pl1 = new Platform(name: "Testing 321")
        def projectData = DataFactory.project()
        def project = projectService.save(new Project(name: projectData.name, code: projectData.code, platforms: [pl, pl1]))

        when:
        go "/project/${project.id}/testCase/create"

        then: "correct options are populated"
        CreateTestCasePage page = browser.page(CreateTestCasePage)
        verifyAll {
            page.platformOptions*.text().containsAll(project.platforms*.name)
            page.platformSelect().selected == ""
        }
    }
}
