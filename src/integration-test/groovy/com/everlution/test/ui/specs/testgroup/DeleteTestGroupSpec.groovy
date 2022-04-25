package com.everlution.test.ui.specs.testgroup

import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testgroup.ListTestGroupPage
import com.everlution.test.ui.support.pages.testgroup.ShowTestGroupPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

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
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }
}
