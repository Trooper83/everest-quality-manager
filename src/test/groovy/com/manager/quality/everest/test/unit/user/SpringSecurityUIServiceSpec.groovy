package com.manager.quality.everest.test.unit.user

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.PersonRole
import com.manager.quality.everest.domains.Role
import com.manager.quality.everest.services.person.SpringSecurityUiService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

class SpringSecurityUIServiceSpec extends Specification implements ServiceUnitTest<SpringSecurityUiService>, DataTest {

    def setupSpec() {
        mockDomains(Person, PersonRole, Role)
    }

    void "addUserRoles adds roles to person"() {
        given:
        Person p = new Person(email: "fakeperson@fake.com", password: "!Testing123").save()

        expect:
        p.getAuthorities().empty

        when:
        def s = new HashSet<Role>()
        def r = new Role(authority: "ROLE_FAKE").save()
        s.add(r)
        service.addUserRoles(p, s)

        then:
        p.getAuthorities().contains(r)
    }

    void "adUserRoles does not throw exception when user null"() {
        when:
        service.addUserRoles(null, new HashSet<Role>())

        then:
        noExceptionThrown()
    }

    void "adUserRoles does not throw exception when roles null"() {
        when:
        Person p = new Person(email: "fakeperson123@fake.com", password: "!Testing123").save()
        service.addUserRoles(p, null)

        then:
        noExceptionThrown()
    }
}