package com.everlution.test.step

import com.everlution.Project
import com.everlution.Step
import com.everlution.StepLink
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class StepLinkSpec extends Specification implements DomainUnitTest<StepLink> {

    void "linkedStep cannot be null"() {
        when:
        domain.linkedStep = null

        then:
        !domain.validate(["linkedStep"])
    }

    void "owner cannot be null"() {
        when:
        domain.owner = null

        then:
        !domain.validate(["owner"])
    }

    void "test instances are persisted"() {
        setup:
        new StepLink(linkedStep: new Step(), owner: new Step(), project: new Project(), relation: 'SIBLING').save()

        expect:
        StepLink.count() == 1
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
        option << ['SIBLING', 'PARENT_CHILD', 'CHILD_PARENT']
    }

    void "relation does not validate with option not in list"() {
        when:
        domain.relation = 'test'

        then:
        !domain.validate(['relation'])
    }
}
