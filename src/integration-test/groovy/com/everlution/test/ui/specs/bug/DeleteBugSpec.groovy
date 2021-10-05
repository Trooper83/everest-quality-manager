package com.everlution.test.ui.specs.bug

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.ProjectService
import com.everlution.test.ui.support.data.Usernames
import com.everlution.test.ui.support.pages.bug.ListBugPage
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class DeleteBugSpec extends GebSpec {

    BugService bugService
    ProjectService projectService

    int id

    def setup() {
        def project = projectService.list(max: 1).first()
        def bug = new Bug(name: "delete bug", creator: "testing creator", project: project)
        id = bugService.save(bug).id
    }

    void "authorized users can delete bug"(String username, String password) {
        given: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/bug/show/${id}"

        when: "delete bug"
        def showPage = browser.at(ShowBugPage)
        showPage.deleteBug()

        then: "at list page"
        at ListBugPage

        where:
        username                         | password
        Usernames.BASIC.username         | "password"
        Usernames.PROJECT_ADMIN.username | "password"
        Usernames.ORG_ADMIN.username     | "password"
        Usernames.APP_ADMIN.username     | "password"
    }
}
