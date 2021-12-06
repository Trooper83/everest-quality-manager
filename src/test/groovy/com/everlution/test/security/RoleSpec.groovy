package com.everlution.test.security

import com.everlution.Role
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class RoleSpec extends Specification implements DomainUnitTest<Role> {

    void "test instances are persisted"() {
        setup: "save two instances"
        new Role(authority: "authority").save()
        new Role(authority: "second authority").save()

        expect: "count is 2"
        Role.count() == 2
    }

    void "test domain instance"() {
        when: "set domain name"
        domain.authority = "auth"

        then: "domain equals the previous set value"
        domain.authority == "auth"
    }

    void "authority cannot be null"() {
        when: "property null"
        domain.authority = null

        then: "validation fails"
        !domain.validate(["authority"])
        domain.errors["authority"].code == "nullable"
    }

    void "authority cannot be blank"() {
        when: "property blank"
        domain.authority = ""

        then: "validation fails"
        !domain.validate(["authority"])
        domain.errors["authority"].code == "blank"
    }

    void "role authority must be unique"() {
        given: "create person instance"
        new Role(authority: "authority").save()

        when: "create a role with same authority"
        def failed = new Role(authority: "authority")

        then: "fails validation"
        !failed.validate(["authority"])

        and: "unique error"
        failed.errors["authority"].code == "unique"

        and: "save fails"
        !failed.save()
        Role.count() == old(Role.count())
    }
}
