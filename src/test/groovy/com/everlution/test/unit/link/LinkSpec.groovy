package com.everlution.test.unit.link

import com.everlution.domains.Project
import com.everlution.domains.Link
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class LinkSpec extends Specification implements DomainUnitTest<Link> {

    void "linkedStep cannot be null"() {
        when:
        domain.linkedId = null

        then:
        !domain.validate(["linkedId"])
    }

    void "owner cannot be null"() {
        when:
        domain.ownerId = null

        then:
        !domain.validate(["ownerId"])
    }

    void "test instances are persisted"() {
        setup:
        new Link(linkedId: 1, ownerId: 2, project: new Project(), relation: 'Is Sibling of').save()

        expect:
        Link.count() == 1
    }

    void "project cannot be null"() {
        when:
        domain.project = null

        then:
        !domain.validate(["project"])
    }

    void "relation cannot be null"() {
        when:
        domain.relation = null

        then:
        !domain.validate(["relation"])
    }

    void "relation validates with option in list"(String option) {
        when:
        domain.relation = option

        then:
        domain.validate(["relation"])

        where:
        option << ['Is Sibling of', 'Is Parent of', 'Is Child of']
    }

    void "relation does not validate with option not in list"() {
        when:
        domain.relation = 'test'

        then:
        !domain.validate(['relation'])
    }
}
