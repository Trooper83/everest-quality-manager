package com.manager.quality.everest.test.ui.functional.specs.bug.create

import com.manager.quality.everest.domains.Area
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.services.project.ProjectService
import com.manager.quality.everest.test.support.DataFactory
import com.manager.quality.everest.test.support.results.SendResults
import com.manager.quality.everest.test.support.data.Credentials
import com.manager.quality.everest.test.ui.support.pages.bug.CreateBugPage
import com.manager.quality.everest.test.ui.support.pages.common.LoginPage
import geb.spock.GebSpec
import grails.testing.mixin.integration.Integration

@SendResults
@Integration
class CreatePageAreaSpec extends GebSpec {

    ProjectService projectService

    void "area options equal project options"() {
        given:
        def area = new Area(name: "Area is the only area")
        def pd = DataFactory.project()
        def project = projectService.save(new Project(name: pd.name, code: pd.code,
                areas: [area]))

        and: "login as a basic user"
        to LoginPage
        LoginPage loginPage = browser.page(LoginPage)
        loginPage.login(Credentials.BASIC.email, Credentials.BASIC.password)

        when:
        def page = to CreateBugPage, project.id

        then:
        page.areaSelect().selectedText == ""
        page.areaSelect().selected == ""
        page.areaOptions*.text().containsAll("Area is the only area")
    }
}
