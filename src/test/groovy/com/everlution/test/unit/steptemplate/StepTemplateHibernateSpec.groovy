package com.everlution.test.unit.steptemplate

import com.everlution.domains.Person
import com.everlution.domains.Project
import com.everlution.domains.StepTemplate
import grails.test.hibernate.HibernateSpec
import spock.lang.Shared

class StepTemplateHibernateSpec extends HibernateSpec {

    @Shared Person person
    @Shared Project project

    def setup() {
        person = new Person(email: "test@test123.com", password: "!Password2022").save()
        project = new Project(name: "Delete Bug Cascade Project777", code: "ZZ7").save()
    }

    void "dateCreated auto generated"() {
        when:
        StepTemplate t = new StepTemplate(name: "testing", act: "do something", result: "something happened",
                person: person, project: project).save()

        then:
        t.dateCreated != null
    }

    void "lastUpdated auto generated"() {
        given:
        StepTemplate t = new StepTemplate(act: "do something123", result: "something happened", person: person,
                name: "testing", project: project).save()

        expect:
        t.lastUpdated != null

        when:
        t.name = "name"
        t.save(flush: true)

        then:
        t.lastUpdated != null
    }
}
