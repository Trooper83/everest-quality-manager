package com.manager.quality.everest.test.ui.functional.specs.project.edit

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.project.EditProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class EditPageSpec extends GebSpec {

    ProjectService projectService
    @Shared Project project

    def setup() {
        project = DataFactory.createProject()
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)
        go "/project/edit/${project.id}"
    }

    void "error message displayed when adding two areas with the same name to project"() {
        when: "add two areas with same name"
        EditProjectPage page = at EditProjectPage
        page.addAreaTag("fail")
        page.addAreaTag("fail")
        page.editProject()

        then: "message displayed"
        page.errorMessages.text() ==
                "Property [areas] with value [[com.manager.quality.everest.domains.Area : (unsaved), com.manager.quality.everest.domains.Area : (unsaved)]] does not pass custom validation"
    }

    void "error message displayed when adding two envs with the same name to project"() {
        when: "add two envs with same name"
        EditProjectPage page = at EditProjectPage
        page.addEnvironmentTag("fail")
        page.addEnvironmentTag("fail")
        page.editProject()

        then: "message displayed"
        page.errorMessages.text() ==
                "Property [environments] with value [[com.manager.quality.everest.domains.Environment : (unsaved), com.manager.quality.everest.domains.Environment : (unsaved)]] does not pass custom validation"
    }
}
