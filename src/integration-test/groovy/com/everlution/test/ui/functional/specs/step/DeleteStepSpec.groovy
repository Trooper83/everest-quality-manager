package com.everlution.test.ui.functional.specs.step

import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.Step
import com.everlution.StepService
import com.everlution.test.ui.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.step.ListStepPage
import com.everlution.test.ui.support.pages.step.ShowStepPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class DeleteStepSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepService stepService

    void "authorized users can delete"(String username, String password) {
        given: "log in as authorized user"
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def step = new Step(name: "name", act: "action", result: "result", project: project, person: person, isBuilderStep: true)
        def id = stepService.save(step).id

        and:
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/project/${project.id}/step/show/${id}"

        when: "delete item"
        def showPage = browser.at(ShowStepPage)
        showPage.delete()

        then: "at list page"
        at ListStepPage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.ORG_ADMIN.email     | Credentials.ORG_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }
}
