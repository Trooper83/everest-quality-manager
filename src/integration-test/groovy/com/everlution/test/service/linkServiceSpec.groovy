package com.everlution.test.service

import com.everlution.Person
import com.everlution.Project
import com.everlution.Relationship
import com.everlution.Step
import com.everlution.Link
import com.everlution.LinkService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Integration
class linkServiceSpec extends Specification {

    @Shared Person person
    @Shared Project project
    @Shared Step parent

    LinkService linkService

    private void setupData() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
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
        setupData()
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: step.id, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)

        expect:
        Link.get(link.id) != null

        when:
        linkService.delete(link.id)

        then:
        Link.get(link.id) == null
    }

    void "save with valid object returns instance"() {
        given:
        setupData()
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: step.id, project: project, relation: Relationship.IS_PARENT_OF.name)

        expect:
        link.id == null

        when:
        def saved = linkService.save(link)

        then:
        saved.id != null
    }

    void "get links returns all items"() {
        setup:
        setupData()

        when:
        def l = linkService.getLinks(parent.id, project)

        then:
        l.size() == 5
    }

    void "create save saves links"() {
        given:
        setupData()
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: step.id, project: project, relation: Relationship.IS_PARENT_OF.name)

        expect:
        link.id == null

        when:
        def saved = linkService.createSave(link)

        then:
        link.id != null
    }
}
