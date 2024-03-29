package com.everlution.test.service

import com.everlution.domains.Person
import com.everlution.domains.Role
import com.everlution.services.person.PersonService
import com.everlution.services.person.SpringSecurityUiService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import org.hibernate.SessionFactory
import spock.lang.Specification

@Integration
@Rollback
class SpringSecurityUiServiceSpec extends Specification {

    PersonService personService
    SessionFactory sessionFactory
    SpringSecurityUiService springSecurityUiService

    void "removeUserRole deletes roles from person"() {
        given:
        def email = "testingemail123@fakeemail.com"
        def p = new Person(email: email, password: "!Password#2024").save()
        def r = new Role(authority: "ROLE_FAKE").save()
        def s = new HashSet<Role>()
        s.add(r)
        springSecurityUiService.addUserRoles(p, s)
        sessionFactory.currentSession.flush()

        expect:
        personService.findByEmail(email).getAuthorities().contains(r)

        when:
        springSecurityUiService.removeUserRole(p, r)

        then:
        !personService.findByEmail(email).getAuthorities().contains(r)
    }

    void "addUserRoles adds roles to person"() {
        given:
        def email = "testingemail@fakeemail.com"
        def p = new Person(email: email, password: "!Password#2024").save()
        def r = new Role(authority: "ROLE_FAKE").save()
        def s = new HashSet<Role>()
        s.add(r)

        when:
        springSecurityUiService.addUserRoles(p, s)
        sessionFactory.currentSession.flush()

        then:
        personService.findByEmail(email).getAuthorities().contains(r)
    }
}
