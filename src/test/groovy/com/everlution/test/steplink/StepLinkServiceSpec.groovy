package com.everlution.test.steplink

import com.everlution.Person
import com.everlution.Project
import com.everlution.Relationship
import com.everlution.Step
import com.everlution.StepLink
import com.everlution.StepLinkService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class StepLinkServiceSpec extends Specification implements ServiceUnitTest<StepLinkService>, DataTest {

    @Shared Person person
    @Shared Project project
    @Shared Step parent

    def setup() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
    }

    def cleanup() {
    }

    def setupSpec() {
        mockDomains(Step, Person, Project, StepLink)
    }

    private void setupData() {
        def child = new Step(name: 'first name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        parent = new Step(name: 'second name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        def uncle = new Step(name: 'third name', act: "action", result: "result", person: person, project: project,
                isBuilderStep: true).save()
        def gParent = new Step(name: "fourth name", act: "action", result: "result", person: person, project: project).save()
        def ggParent = new Step(name: "fifth name", act: "action", result: "result", person: person, project: project).save()
        new StepLink(owner: parent, linkedStep: child, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new StepLink(owner: child, linkedStep: parent, project: project, relation: Relationship.IS_CHILD_OF.name).save()
        new StepLink(owner: parent, linkedStep: uncle, project: project, relation: Relationship.IS_SIBLING_OF.name).save()
        new StepLink(owner: uncle, linkedStep: child, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new StepLink(owner: gParent, linkedStep: uncle, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new StepLink(owner: gParent, linkedStep: parent, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new StepLink(owner: parent, linkedStep: gParent, project: project, relation: Relationship.IS_CHILD_OF.name).save()
        new StepLink(owner: ggParent, linkedStep: gParent, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)
    }

    void "delete with valid id deletes instance"() {
        given:
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new StepLink(owner: s, linkedStep: step, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)

        expect:
        StepLink.get(link.id) != null

        when:
        service.delete(link.id)
        currentSession.flush()

        then:
        StepLink.get(link.id) == null
    }

    void "save with valid object returns instance"() {
        given:
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new StepLink(owner: s, linkedStep: step, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)

        when:
        def saved = service.save(link)

        then:
        saved instanceof StepLink
    }

    void "save with invalid object throws validation exception"() {
        given:
        def link = new StepLink()

        when:
        service.save(link)

        then:
        thrown(ValidationException)
    }

    void "get related steps returns all items"() {
        setup:
        setupData()

        when:
        def map = service.getStepLinksByType(parent)

        then:
        map.children.size() == 1
        map.parents.size() == 1
        map.siblings.size() == 1
    }

    void "get related steps returns empty map when step is null"() {
        setup:
        setupData()

        when:
        def map = service.getStepLinksByType(null)

        then:
        noExceptionThrown()
        map.isEmpty()
    }

    void "get related steps does not throw exception when step is null"() {
        setup:
        setupData()

        when:
        def map = service.getStepLinksByType(null)

        then:
        noExceptionThrown()
    }

    void "get step links returns all items"() {
        setup:
        setupData()

        when:
        def l = service.getStepLinks(parent)

        then:
        l.size() == 5
    }

    void "get step links returns empty map when step is null"() {
        setup:
        setupData()

        when:
        def l = service.getStepLinks(null)

        then:
        noExceptionThrown()
        l.isEmpty()
    }

    void "get step links does not throw exception when step is null"() {
        setup:
        setupData()

        when:
        def l = service.getStepLinks(null)

        then:
        noExceptionThrown()
    }
}
