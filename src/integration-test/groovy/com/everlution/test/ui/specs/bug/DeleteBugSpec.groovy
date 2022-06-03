package com.everlution.test.ui.specs.bug

import com.everlution.Bug
import com.everlution.BugService
import com.everlution.PersonService
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
    PersonService personService
    ProjectService projectService

    void "authorized users can delete bug"(String username, String password) {
        given: "log in as authorized user"
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def bug = new Bug(name: "delete bug", person: person, project: project, status: "Open")
        def id = bugService.save(bug).id

        and:
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/project/${project.id}/bug/show/${id}"

        when: "delete bug"
        def showPage = browser.at(ShowBugPage)
        showPage.delete()

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
