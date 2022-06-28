package com.everlution.test.security

import com.everlution.Person
import com.everlution.PersonRole
import com.everlution.Role
import grails.testing.gorm.DataTest
import spock.lang.Specification

class PersonIntSpec extends Specification implements DataTest {

    Class<?>[] getDomainClassesToMock(){
        return [Person, PersonRole, Role] as Class[]
    }

    void "getAuthorities returns no roles for person with no roles"() {
        given:
        Person p = new Person(email: "test@test.com", password: "!Password2022").save()

        expect:
        p.getAuthorities().empty
    }

    void "getAuthorities returns all roles for person"() {
        given:
        Person person = new Person(email: "test@test.com", password: "!Password2022").save()
        def role = new Role(authority: "TEST_ROLE").save()
        new PersonRole(person: person, role: role).save()

        expect:
        def auths = person.getAuthorities()
        auths.size() == 1
        auths.first() == role
    }
}
