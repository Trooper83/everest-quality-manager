package com.everlution.test.step

import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import grails.test.hibernate.HibernateSpec
import spock.lang.Shared

class StepHibernateSpec extends HibernateSpec {

    @Shared Person person
    @Shared Project project

    def setup() {
        person = new Person(email: "test@test123.com", password: "!Password2022").save()
        project = new Project(name: "Delete Bug Cascade Project777", code: "ZZ7").save()
    }

    void "dateCreated auto generated"() {
        when:
        Step testStep = new Step(act: "do something", result: "something happened", person: person, project: project).save()

        then:
        testStep.dateCreated != null
    }

    void "lastUpdated auto generated"() {
        given:
        Step testStep = new Step(act: "do something123", result: "something happened", person: person, project: project).save()

        expect:
        testStep.lastUpdated != null

        when:
        testStep.name = "name"
        testStep.save(flush: true)

        then:
        testStep.lastUpdated != null
    }
}
