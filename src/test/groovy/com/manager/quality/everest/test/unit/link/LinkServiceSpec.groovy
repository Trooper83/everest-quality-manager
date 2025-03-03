package com.manager.quality.everest.test.unit.link

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.Relationship
import com.manager.quality.everest.domains.Step
import com.manager.quality.everest.domains.Link
import com.manager.quality.everest.services.link.LinkService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class LinkServiceSpec extends Specification implements ServiceUnitTest<LinkService>, DataTest {

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
        mockDomains(Step, Person, Project, Link)
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
        new Link(ownerId: parent.id, linkedId: child.id, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: child.id, linkedId: parent.id, project: project, relation: Relationship.IS_CHILD_OF.name).save()
        new Link(ownerId: parent.id, linkedId: uncle.id, project: project, relation: Relationship.IS_SIBLING_OF.name).save()
        new Link(ownerId: uncle.id, linkedId: child.id, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: gParent.id, linkedId: uncle.id, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: gParent.id, linkedId: parent.id, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: parent.id, linkedId: gParent.id, project: project, relation: Relationship.IS_CHILD_OF.name).save()
        new Link(ownerId: ggParent.id, linkedId: gParent.id, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)
    }

    void "delete with valid id deletes instance"() {
        given:
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: step.id, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)

        expect:
        Link.get(link.id) != null

        when:
        service.delete(link.id)
        currentSession.flush()

        then:
        Link.get(link.id) == null
    }

    void "save with valid object returns instance"() {
        given:
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: step.id, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)

        when:
        def saved = service.save(link)

        then:
        saved instanceof Link
    }

    void "save with invalid object throws validation exception"() {
        given:
        def link = new Link()

        when:
        service.save(link)

        then:
        thrown(ValidationException)
    }

    void "get links returns all items"() {
        setup:
        setupData()

        when:
        def l = service.getLinks(parent.id, project)

        then:
        l.size() == 5
    }

    void "get links returns empty map when id is null"() {
        setup:
        setupData()

        when:
        def l = service.getLinks(null, new Project())

        then:
        noExceptionThrown()
        l.isEmpty()
    }

    void "get links does not throw exception when id is null"() {
        setup:
        setupData()

        when:
        service.getLinks(null, new Project())

        then:
        noExceptionThrown()
    }

    void "get links returns empty map when project is null"() {
        setup:
        setupData()

        when:
        def l = service.getLinks(1, null)

        then:
        noExceptionThrown()
        l.isEmpty()
    }

    void "get links does not throw exception when project is null"() {
        setup:
        setupData()

        when:
        service.getLinks(1, null)

        then:
        noExceptionThrown()
    }

    void "create save adds bi-directional sibling links"() {
        given:
        setupData()
        Step testStep = new Step(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        Step testStep1 = new Step(name: "should be returned", act: "do something", result: "something happened",
                person: person, project: project).save()
        def link = new Link(ownerId: testStep.id, linkedId: testStep1.id, project: project,
                relation: Relationship.IS_SIBLING_OF.name)

        when:
        service.createSave(link)

        then:
        def links = service.getLinks(testStep.id, project)
        links.size() == 2
        links.get(1).ownerId == testStep1.id
        links.get(1).linkedId == testStep.id
        links.get(1).relation == Relationship.IS_SIBLING_OF.name
    }

    void "create save adds bi-directional parental links"() {
        given:
        setupData()
        Step testStep = new Step(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        Step testStep1 = new Step(name: "should be returned", act: "do something", result: "something happened",
                person: person, project: project).save()
        def link = new Link(ownerId: testStep.id, linkedId: testStep1.id, project: project,
                relation: Relationship.IS_PARENT_OF.name)

        when:
        service.createSave(link)

        then:
        def links = service.getLinks(testStep.id, project)
        links.size() == 2
        links.get(1).ownerId == testStep1.id
        links.get(1).linkedId == testStep.id
        links.get(1).relation == Relationship.IS_CHILD_OF.name
    }

    void "create save adds bi-directional children links"() {
        given:
        setupData()
        Step testStep = new Step(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        Step testStep1 = new Step(name: "should be returned", act: "do something", result: "something happened",
                person: person, project: project).save()
        def link = new Link(ownerId: testStep.id, linkedId: testStep1.id, project: project,
                relation: Relationship.IS_CHILD_OF.name)

        when:
        service.createSave(link)

        then:
        def links = service.getLinks(testStep.id, project)
        links.size() == 2
        links.get(1).ownerId == testStep1.id
        links.get(1).linkedId == testStep.id
        links.get(1).relation == Relationship.IS_PARENT_OF.name
    }

    void "create save does not add bi-directional links when linking to self"() {
        given:
        setupData()
        Step testStep = new Step(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        def link = new Link(ownerId: testStep.id, linkedId: testStep.id, project: project,
                relation: Relationship.IS_CHILD_OF.name)

        when:
        service.createSave(link)

        then:
        def links = service.getLinks(testStep.id, project)
        links.size() == 1
        links.get(0).ownerId == testStep.id
        links.get(0).linkedId == testStep.id
        links.get(0).relation == Relationship.IS_CHILD_OF.name
    }

    void "create save does not create duplicate links"() {
        given:
        setupData()
        Step testStep = new Step(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        def link1 = new Link(ownerId: testStep.id, linkedId: testStep.id, project: project,
                relation: Relationship.IS_CHILD_OF.name)
        def link2 = new Link(ownerId: testStep.id, linkedId: testStep.id, project: project,
                relation: Relationship.IS_CHILD_OF.name)

        when:
        service.createSave(link1)
        service.createSave(link2)

        then:
        def links = service.getLinks(testStep.id, project)
        links.size() == 1
        links.get(0).ownerId == testStep.id
        links.get(0).linkedId == testStep.id
        links.get(0).relation == Relationship.IS_CHILD_OF.name
    }

    void "validation fails for invalid relation type"() {
        given:
        setupData()
        Step testStep = new Step(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        Step testStep1 = new Step(name: "should be returned", act: "do something", result: "something happened",
                person: person, project: project).save()
        def link = new Link(ownerId: testStep.id, linkedId: testStep1.id, project: project,
                relation: 'testing')

        when:
        service.createSave(link)

        then:
        thrown(ValidationException)
    }

    void "read returns instance"() {
        setup:
        setupData()

        expect:
        service.read(parent.id) != null
    }

    void "delete related links deletes link and inverted link"() {
        given:
        Step testStep = new Step(name: "should not be here", act: "do something", result: "something happened",
                person: person, project: project).save()
        Step testStep1 = new Step(name: "should be returned", act: "do something", result: "something happened",
                person: person, project: project).save()
        def link = new Link(ownerId: testStep.id, linkedId: testStep1.id, project: project,
                relation: Relationship.IS_CHILD_OF.name)

        and:
        service.createSave(link)

        expect:
        Link.count() == 2

        when:
        service.deleteRelatedLinks([link.id])

        then:
        Link.count() == 0
    }

    void "delete related links does not throw null pointer when list null"() {
        when:
        service.deleteRelatedLinks(null)

        then:
        notThrown(NullPointerException)
    }
}
