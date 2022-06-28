package com.everlution.test.scenario

import com.everlution.Person
import com.everlution.Project
import com.everlution.Scenario
import grails.test.hibernate.HibernateSpec
import org.springframework.dao.InvalidDataAccessApiUsageException
import spock.lang.Shared

class ScenarioHibernateSpec extends HibernateSpec {

    @Shared Project project
    @Shared Person person

    def setup() {
        project = new Project(name: "scn domain project", code: "sdp").save()
        person = new Person(email: "scnPerson@scn.com", password: "!Password2022").save()
    }

    void "date created auto generated"() {
        when:
        def scn = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then:
        scn.dateCreated != null
    }

    void "save does not cascade to project"() {
        when: "unsaved project is added to scenario"
        Project project = new Project(name: "Scenario Project2", code: "BMP")
        new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "save does not cascade to person"() {
        when: "unsaved person is added to scenario"
        def p = new Person(email: "fail@fail.com", password: "password")
        new Scenario(person: p, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        then: "exception is thrown"
        thrown(InvalidDataAccessApiUsageException)
    }

    void "delete scenario does not cascade to project"() {
        given:
        def scn = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()

        expect:
        Scenario.findById(scn.id) != null

        when: "delete scenario"
        scn.delete()
        sessionFactory.currentSession.flush()

        then: "project is not deleted"
        Scenario.findById(scn.id) == null
        Project.findById(project.id) != null
    }
}
