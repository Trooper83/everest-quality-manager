package com.everlution.test.step

import com.everlution.Project
import com.everlution.Step
import com.everlution.StepLink
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class StepLinkSpec extends Specification implements DomainUnitTest<StepLink> {

    void "child cannot be null"() {
        when:
        domain.child == null

        then:
        !domain.validate(["child"])
    }

    void "parent cannot be null"() {
        when:
        domain.parent == null

        then:
        !domain.validate(["parent"])
    }

    void "test instances are persisted"() {
        setup:
        new StepLink(child: new Step(), parent: new Step(), project: new Project()).save()

        expect:
        StepLink.count() == 1
    }

    void "project cannot be null"() {
        when:
        domain.project == null

        then:
        !domain.validate(["project"])
    }
}
