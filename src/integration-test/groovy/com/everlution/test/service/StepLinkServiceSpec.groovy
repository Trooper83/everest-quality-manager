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
class StepLinkServiceSpec extends Specification {

    @Shared Person person
    @Shared Project project
    @Shared Step parent

    LinkService stepLinkService

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
        new Link(ownerId: parent, linkedId: child, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: child, linkedId: parent, project: project, relation: Relationship.IS_CHILD_OF.name).save()
        new Link(ownerId: parent, linkedId: uncle, project: project, relation: Relationship.IS_SIBLING_OF.name).save()
        new Link(ownerId: uncle, linkedId: child, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: gParent, linkedId: uncle, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: gParent, linkedId: parent, project: project, relation: Relationship.IS_PARENT_OF.name).save()
        new Link(ownerId: parent, linkedId: gParent, project: project, relation: Relationship.IS_CHILD_OF.name).save()
        new Link(ownerId: ggParent, linkedId: gParent, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)
    }

    void "delete with valid id deletes instance"() {
        given:
        setupData()
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s, linkedId: step, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)

        expect:
        Link.get(link.id) != null

        when:
        stepLinkService.delete(link.id)

        then:
        Link.get(link.id) == null
    }

    void "save with valid object returns instance"() {
        given:
        setupData()
        def s = new Step(act: "action", result: "result", person: person, project: project).save()
        def step = new Step(act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s, linkedId: step, project: project, relation: Relationship.IS_PARENT_OF.name)

        expect:
        link.id == null

        when:
        def saved = stepLinkService.save(link)

        then:
        saved.id != null
    }

    void "get related steps returns all items"() {
        setup:
        setupData()

        when:
        def map = stepLinkService.getStepLinksByType(parent)

        then:
        map.children.size() == 1
        map.parents.size() == 1
        map.siblings.size() == 1
    }
}
