package com.everlution.test.ui.functional.specs.project.home

import com.everlution.domains.Bug
import com.everlution.services.bug.BugService
import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.domains.ReleasePlan
import com.everlution.services.releaseplan.ReleasePlanService
import com.everlution.test.support.DataFactory
import com.everlution.test.support.data.Credentials
import com.everlution.test.support.results.SendResults
import com.everlution.test.ui.support.pages.bug.ShowBugPage
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.project.CreateProjectPage
import com.everlution.test.ui.support.pages.project.EditProjectPage
import com.everlution.test.ui.support.pages.project.ProjectHomePage
import com.everlution.test.ui.support.pages.project.ShowProjectPage
import com.everlution.test.ui.support.pages.releaseplan.ShowReleasePlanPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration
import spock.lang.Shared

import java.sql.Timestamp

@SendResults
@Integration
class HomePageSpec extends GebSpec {

    @Shared int id

    BugService bugService
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

    void "maximum of ten bugs are displayed"() {
        given:
        def person = personService.list(max:1).first()
        def p = projectService.list(max: 1).first()
        int count = 0
        while(count < 15) {
            bugService.save(new Bug(project: p, name: "Bug number ${count}", status: "Open", person: person))
            count++
        }

        when:
        def page = to ProjectHomePage, p.id

        then:
        page.recentBugsTable.rowCount == 10
    }

    void "bug links direct to show bug view"() {
        given:
        def person = personService.list(max:1).first()
        def p = projectService.list(max: 1).first()
        bugService.save(new Bug(project: p, name: "Bug Links correctly to show page", status: "Open", person: person))

        when:
        def page = to ProjectHomePage, p.id
        page.recentBugsTable.clickCell("Name", 0)

        then:
        at ShowBugPage
    }

    void "previous release link directs to show release plan view"() {
        given:
        def person = personService.list(max:1).first()
        def project = projectService.list(max: 1).first()

        def plan = new ReleasePlan(name: "Released test plan", project: project, status: "Released", person: person,
                                    releaseDate: new Timestamp(System.currentTimeMillis()))
        releasePlanService.save(plan)

        when:
        def page = to ProjectHomePage, project.id
        page.goToReleasedPlan()

        then:
        at ShowReleasePlanPage
    }

    void "current release link directs to show release plan view"() {
        given:
        def person = personService.list(max:1).first()
        def project = projectService.list(max: 1).first()

        def plan = new ReleasePlan(name: "In Progress test plan", project: project, status: "In Progress",
                                    person: person, plannedDate: new Timestamp(System.currentTimeMillis()))
        releasePlanService.save(plan)

        when:
        def page = to ProjectHomePage, project.id
        page.goToInProgressPlan()

        then:
        at ShowReleasePlanPage
    }

    void "not found text displayed when no release plans found"() {
        given:
        def p = DataFactory.createProject()

        when:
        def page = to ProjectHomePage, p.id

        then:
        page.releasedPlansNotFoundText.displayed
        page.inProgressPlansNotFoundText.displayed
    }

    void "dates are formatted as MMMM d yyyy"() {
        given:
        def person = personService.list(max:1).first()
        def p = projectService.list(max: 1).first()
        bugService.save(new Bug(project: p, name: "Bug Links correctly to show page", status: "Open", person: person))
        def rPlan = new ReleasePlan(name: "Released test plan", project: p, status: "Released", person: person,
                releaseDate: new Timestamp(System.currentTimeMillis()))
        def iPlan = new ReleasePlan(name: "In Progress test plan", project: p, status: "In Progress",
                person: person, plannedDate: new Timestamp(System.currentTimeMillis()))
        releasePlanService.save(rPlan)
        releasePlanService.save(iPlan)

        when:
        def page = to ProjectHomePage, p.id

        then:
        def d = DataFactory.getPastDate(0)
        page.recentBugsTable.getValueInColumn(0, "Created") == d
        page.inProgressPlanLinks.first().find("[data-name=createdDateValue]").text() == d
        page.releasedPlanLinks.first().find("[data-name=createdDateValue]").text() == d
    }
}
