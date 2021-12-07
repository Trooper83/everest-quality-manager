package com.everlution.test.security

import com.everlution.Person
import com.everlution.PersonRole
import com.everlution.Role
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PersonRoleSpec extends Specification implements DomainUnitTest<PersonRole> {

    void "test domain instance"() {
        given:
        def p = Mock(Person)

        when: "set domain person"
        domain.person = p

        then: "domain equals the previous set value"
        domain.person == p
    }

    void "person cannot be null"() {
        when: "property null"
        domain.person = null

        then: "validation fails"
        !domain.validate(["person"])
        domain.errors["person"].code == "nullable"
    }

    void "role cannot be null"() {
        when: "property null"
        domain.role = null

        then: "validation fails"
        !domain.validate(["role"])
        domain.errors["role"].code == "nullable"
    }

    void "test instances are persisted"() {
        setup: "save two instances"
        def person = new Person(email: "test@test.com", password: "password")
        def person1 = new Person(email: "test1@test.com", password: "password")
        def role = new Role(authority: "TEST_ROLE")
        new PersonRole(person: person, role: role).save()
        new PersonRole(person: person1, role: role).save()

        expect: "count is 2"
        PersonRole.count() == 2
    }

    void "role validates with unsaved person"() {
        given: "unsaved person"
        def person = new Person(email: "test@test.com", password: "pass")
        def role = new Role(authority: "TEST_ROLE").save()

        when:
        domain.person = person
        domain.role = role

        then:
        domain.validate(["role"])
    }

    void "role validates with saved person"() {
        given: "unsaved person"
        def person = new Person(email: "test@test.com", password: "pass").save()
        def role = new Role(authority: "TEST_ROLE").save()

        when:
        domain.person = person
        domain.role = role

        then:
        domain.validate(["role"])
    }

    void "role validates with null person"() {
        given: "saved role"
        def role = new Role(authority: "TEST_ROLE").save()

        when:
        domain.person = null
        domain.role = role

        then:
        domain.validate(["role"])
    }

    void "role fails validation with personRole that already exists"() {
        given: "saved personRole"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()
        new PersonRole(person: person, role: role).save()

        expect:
        PersonRole.count == 1

        when:
        domain.person = person
        domain.role = role

        then:
        !domain.validate(["role"])
        domain.errors["role"].code == "userRole.exists"
    }

    void "get returns instance"() {
        when: "saved personRole"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()
        def personRole = new PersonRole(person: person, role: role).save()

        then:
        PersonRole.count == 1
        PersonRole.get(person.id, role.id) == personRole
    }

    void "get returns null with invalid criteria"() {
        expect:
        PersonRole.get(1111, 1111) == null
    }

    void "exists returns false with invalid criteria"() {
        expect:
        !PersonRole.exists(1111, 1111)
    }

    void "exists returns true for valid personRole"() {
        when: "saved personRole"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()
        new PersonRole(person: person, role: role).save()

        then:
        PersonRole.exists(person.id, role.id)
    }

    void "create persists instance"() {
        given: "saved role and person"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()

        expect: "personRole not found"
        !PersonRole.exists(person.id, role.id)

        when: "create"
        PersonRole.create(person, role)

        then:
        PersonRole.exists(person.id, role.id)
    }

    void "remove deletes instance"() {
        setup: "saved role and person"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()
        PersonRole.create(person, role)

        expect: "personRole found"
        PersonRole.exists(person.id, role.id)
        PersonRole.remove(person, role)
    }

    void "remove does not delete invalid instance"() {
        setup: "saved role and person"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()

        expect: "personRole not found"
        !PersonRole.remove(person, role)
    }

    void "remove returns false with null person"() {
        setup: "saved role and person"
        def role = new Role(authority: "TEST_ROLE").save()

        expect: "personRole not found"
        !PersonRole.remove(null, role)
    }

    void "remove returns false with null role"() {
        setup: "saved role and person"
        def person = new Person(email: "test@test.com", password: "pass").save()

        expect: "personRole not found"
        !PersonRole.remove(person, null)
    }

    void "removeAll deletes all personRoles for person"() {
        setup: "saved personRole"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()
        PersonRole.create(person, role)

        expect:
        PersonRole.removeAll(person) == 1
    }

    void "removeAll deletes all personRoles for role"() {
        setup: "saved personRole"
        def role = new Role(authority: "TEST_ROLE").save()
        def person = new Person(email: "test@test.com", password: "pass").save()
        PersonRole.create(person, role)

        expect:
        PersonRole.removeAll(role) == 1
    }

    void "removeAll deletes returns 0 when person null"() {
        expect:
        PersonRole.removeAll(null as Person) == 0
    }

    void "removeAll deletes returns 0 when role null"() {
        expect:
        PersonRole.removeAll(null as Role) == 0
    }
}
