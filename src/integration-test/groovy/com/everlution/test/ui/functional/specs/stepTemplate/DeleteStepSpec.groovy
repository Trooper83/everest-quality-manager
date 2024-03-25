package com.everlution.test.ui.functional.specs.stepTemplate

import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.domains.StepTemplate
import com.everlution.services.steptemplate.StepTemplateService
import com.everlution.services.testcase.TestCaseService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.stepTemplate.ListStepTemplatePage
import com.everlution.test.ui.support.pages.stepTemplate.ShowStepTemplatePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class DeleteStepSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    StepTemplateService stepTemplateService
    TestCaseService testCaseService

    void "authorized users can delete"(String username, String password) {
        given: "log in as authorized user"
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def step = new StepTemplate(name: "name", act: "action", result: "result", project: project, person: person)
        def id = stepTemplateService.save(step).id

        and:
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/project/${project.id}/stepTemplate/show/${id}"

        when: "delete item"
        def showPage = browser.at(ShowStepTemplatePage)
        showPage.delete()

        then: "at list page"
        at ListStepTemplatePage

        where:
        username                        | password
        Credentials.BASIC.email         | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email     | Credentials.APP_ADMIN.password
    }
}
