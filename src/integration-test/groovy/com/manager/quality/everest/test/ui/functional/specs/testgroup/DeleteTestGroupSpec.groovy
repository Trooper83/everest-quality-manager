package com.manager.quality.everest.test.ui.functional.specs.testgroup

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.services.testgroup.TestGroupService
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import com.manager.quality.everest.test.ui.support.pages.testgroup.ListTestGroupPage
import com.manager.quality.everest.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

@SendResults
@Integration
class DeleteTestGroupSpec extends GebSpec {

    ProjectService projectService
    TestGroupService testGroupService

    @Shared Long id
    @Shared Project project

    def setup() {
        project = projectService.list(max: 1).first()
        def group = new TestGroup(name: "delete spec group", project: project)
        id = testGroupService.save(group).id
    }

    void "authorized users can delete test group"(String username, String password) {
        given: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/project/${project.id}/testGroup/show/${id}"

        when: "delete instance"
        def showPage = browser.at(ShowTestGroupPage)
        showPage.delete()

        then: "at list page"
        at ListTestGroupPage

        where:
        username                         | password
        Credentials.BASIC.email | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }
}
