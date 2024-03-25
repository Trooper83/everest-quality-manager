package com.everlution.test.service

import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.Relationship
import com.everlution.domains.Link
import com.everlution.services.link.LinkService
import com.everlution.domains.StepTemplate
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Shared
import spock.lang.Specification

@Rollback
@Integration
class LinkServiceSpec extends Specification {

    @Shared Person person
    @Shared Project project
    @Shared StepTemplate parent

    LinkService linkService

    private void setupData() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
        def child = new StepTemplate(name: 'first name', act: "action", result: "result", person: person, project: project).save()
        parent = new StepTemplate(name: 'second name', act: "action", result: "result", person: person, project: project).save()
        def uncle = new StepTemplate(name: 'third name', act: "action", result: "result", person: person, project: project).save()
        def gParent = new StepTemplate(name: "fourth name", act: "action", result: "result", person: person, project: project).save()
        def ggParent = new StepTemplate(name: "fifth name", act: "action", result: "result", person: person, project: project).save()
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
        def s = new StepTemplate(name: '123', act: "action", result: "result", person: person, project: project).save()
        def t = new StepTemplate(name: '234', act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: t.id, project: project, relation: Relationship.IS_PARENT_OF.name).save(flush: true)

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
        def s = new StepTemplate(name: '345', act: "action", result: "result", person: person, project: project).save()
        def t = new StepTemplate(name: '456', act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: t.id, project: project, relation: Relationship.IS_PARENT_OF.name)

        expect:
        link.id == null

        when:
        def saved = linkService.save(link)

        then:
        saved.id != null
    }

    void "getLinks returns all items"() {
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
        def s = new StepTemplate(name: '567', act: "action", result: "result", person: person, project: project).save()
        def t = new StepTemplate(name: '678', act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: t.id, project: project, relation: Relationship.IS_PARENT_OF.name)

        expect:
        link.id == null

        when:
        def saved = linkService.createSave(link)

        then:
        saved != null
    }

    void "read returns link"() {
        expect:
        setupData()
        linkService.read(1) != null
    }

    void "deleteRelatedLinks deletes link and inverted link"() {
        setup:
        setupData()
        def s = new StepTemplate(name: '890', act: "action", result: "result", person: person, project: project).save()
        def step = new StepTemplate(name: '901', act: "action", result: "result", person: person, project: project).save()
        def link = new Link(ownerId: s.id, linkedId: step.id, project: project, relation: Relationship.IS_PARENT_OF.name)

        when:
        linkService.save(link)
        linkService.deleteRelatedLinks([link.id])

        then:
        linkService.read(link.id) == null
    }
}
