package com.everlution.test.ui.functional.specs.project.home

import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.ReleasePlan
import com.everlution.ReleasePlanService
import com.everlution.test.support.data.Credentials

import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

import java.time.Instant
import java.time.temporal.ChronoUnit

@Integration
class HomePageSpec extends GebSpec {

    @Shared int id

    PersonService personService
    ProjectService projectService
    ReleasePlanService releasePlanService

    def setup() {
        id = projectService.list(max: 1).first().id
        given: "login as a project admin user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.PROJECT_ADMIN.email, Credentials.PROJECT_ADMIN.password)
    }

    void "status message displayed after project created"() {
        when: "go to the create page"
        to CreateProjectPage

        and: "create a project"
        CreateProjectPage page = browser.page(CreateProjectPage)
        page.createProject("status message project", "SMP")

        then: "at show page with message displayed"
        def show = at ShowProjectPage
        show.statusMessage.text() ==~ /Project \S+ created/
    }

    void "project not deleted if alert is canceled"() {
        when: "go to show project page"
        go "/project/show/${id}"

        and: "click delete and cancel | verify message"
        def page = browser.page(ShowProjectPage)
        assert withConfirm(false) { page.deleteLink.click() } == "Are you sure?"

        then: "at show page"
        at ShowProjectPage
    }

    void "updated message displays after updating project"() {
        when: "go to edit project page"
        go "/project/edit/${id}"

        and: "edit a project"
        EditProjectPage page = at EditProjectPage
        page.editProject()

        then: "at show page with message displayed"
        def showPage = at ShowProjectPage
        showPage.statusMessage.text() == "Project ${id} updated"
    }

    void "error message displayed when project cannot be deleted with associated items"() {
        given: "a project with a release plan"
        def person = personService.list(max:1).first()
        def project = projectService.list(max: 1).first()

        def plan = new ReleasePlan(name: "test plan", project: project, status: "ToDo", person: person)
        releasePlanService.save(plan)

        and: "go to show project page"
        go "/project/show/${id}"

        when: "delete project"
        def page = at ShowProjectPage
        page.deleteProject()

        then: "message displayed"
        at ShowProjectPage
        page.errorsMessage.text() == "Project has associated items and cannot be deleted"
    }

    void "next and previous links display"() {
        given: "a project with a release plan"
        def person = personService.list(max:1).first()
        def project = projectService.list(max: 1).first()
        def futureDate = new Date().from(Instant.now().plus(1, ChronoUnit.MINUTES))
        def pastDate = new Date().from(Instant.now().minus(1, ChronoUnit.MINUTES))
        def nextPlan = new ReleasePlan(name: "next plan", project: project, status: "ToDo", plannedDate: futureDate,
                person: person)
        def previousPlan = new ReleasePlan(name: "previous plan", project: project, status: "Released",
                releaseDate: pastDate, person: person)
        releasePlanService.save(nextPlan)
        releasePlanService.save(previousPlan)

        when: "go to project page"
        go "/project/home/${project.id}"

        then:
        def page = at ProjectHomePage
        page.nextReleaseLink.displayed
        page.previousReleaseLink.displayed
    }
}
