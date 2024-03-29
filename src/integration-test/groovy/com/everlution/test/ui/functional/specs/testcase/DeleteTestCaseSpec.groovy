package com.everlution.test.ui.functional.specs.testcase

import com.everlution.services.person.PersonService
import com.everlution.services.project.ProjectService
import com.everlution.domains.TestCase
import com.everlution.services.testcase.TestCaseService
import com.everlution.test.support.data.Credentials
import com.everlution.test.ui.support.pages.common.LoginPage
import com.everlution.test.ui.support.pages.testcase.ListTestCasePage
import com.everlution.test.ui.support.pages.testcase.ShowTestCasePage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@Integration
class DeleteTestCaseSpec extends GebSpec {

    PersonService personService
    ProjectService projectService
    TestCaseService testCaseService

    void "authorized users can delete Test Case"(String username, String password) {
        given:
        def project = projectService.list(max: 1).first()
        def person = personService.list(max: 1).first()
        def testCase = new TestCase(name: "test case name", person: person, description: "desc", type: "UI",
                executionMethod: "Manual", project: project)
        def id = testCaseService.save(testCase).id

        and: "log in as authorized user"
        def loginPage = to LoginPage
        loginPage.login(username, password)

        and: "go to show page"
        go "/project/${project.id}/testCase/show/${id}"

        when: "delete test case"
        def showPage = browser.at(ShowTestCasePage)
        showPage.delete()

        then: "at list page"
        at ListTestCasePage

        where:
        username                         | password
        Credentials.BASIC.email          | Credentials.BASIC.password
        Credentials.PROJECT_ADMIN.email  | Credentials.PROJECT_ADMIN.password
        Credentials.APP_ADMIN.email      | Credentials.APP_ADMIN.password
    }
}
