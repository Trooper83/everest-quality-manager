package com.manager.quality.everest.test.unit.step

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.Step
import grails.test.hibernate.HibernateSpec
import spock.lang.Shared

class StepHibernateSpec extends HibernateSpec {

    @Shared Project project

    void "dateCreated auto generated"() {
        when:
        def s = new Step(act: "doing this").save()

        then:
        s.dateCreated != null
    }

    void "lastUpdated auto generated"() {
        given:
        Step testStep = new Step(act: "do something123", result: "something happened").save()

        expect:
        testStep.lastUpdated != null

        when:
        testStep.act = "name"
        testStep.save(flush: true)

        then:
        testStep.lastUpdated != null
    }
}
