package com.everlution.test.step

import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import com.everlution.StepLink
import com.everlution.StepLinkService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class StepLinkServiceSpec extends Specification implements ServiceUnitTest<StepLinkService>, DataTest {

    def setup() {
    }

    def cleanup() {
    }

    def setupSpec() {
        mockDomains(Step, Person, Project, StepLink)
    }

    @Shared Person person
    @Shared Project project
    Step child

    private void setupData() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
        child = new Step(name: 'first name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        def parent = new Step(name: 'second name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        def uncle = new Step(name: 'third name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        def gParent = new Step(name: "fourth name", act: "action", result: "result", person: person, project: project).save()
        def ggParent = new Step(name: "fifth name", act: "action", result: "result", person: person, project: project).save()
        new StepLink(parent: parent, child: child, project: project).save()
        new StepLink(parent: uncle, child: child, project: project).save()
        new StepLink(parent: gParent, child: uncle, project: project).save()
        new StepLink(parent: gParent, child: parent, project: project).save()
        new StepLink(parent: ggParent, child: gParent, project: project).save(flush: true)
    }

    void "test something"() {
        expect:
        false
    }
}
