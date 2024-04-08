package com.everlution.test.unit.testiteration

import com.everlution.domains.Person
import com.everlution.domains.TestIterationResult
import grails.test.hibernate.HibernateSpec

class TestIterationResultHibernateSpec extends HibernateSpec {

    List<Class> getDomainClasses() { [Person, TestIterationResult] }

    void "delete does not cascade to person"() {
        given:
        def p = new Person(email: "testing@testing.com", password: "what-Is-123-Passw#4fj").save()
        def r = new TestIterationResult(person: p, result: "FAILED").save()

        when:
        r.delete()

        then:
        Person.get(p.id) != null
    }

    void "dateCreated is populated when saved"() {
        when:
        def p = new Person(email: "testing@testing.com", password: "what-Is-123-Passw#4fj").save()
        def t = new TestIterationResult(person: p, result: "FAILED").save()

        then:
        t.dateCreated != null
    }
}
