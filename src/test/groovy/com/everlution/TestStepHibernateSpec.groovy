package com.everlution

import grails.test.hibernate.HibernateSpec

class TestStepHibernateSpec extends HibernateSpec {

    void "test date created auto generated"() {
        when:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()

        then:
        testStep.dateCreated != null
    }
}
