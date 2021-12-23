package com.everlution.test.scenario

import com.everlution.Person
import com.everlution.Project
import com.everlution.Scenario
import com.everlution.ScenarioService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Specification

class ScenarioServiceSpec extends Specification implements ServiceUnitTest<ScenarioService>, DataTest {

    def setupSpec() {
        mockDomain(Scenario)
    }

    private void setupData() {
        def project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        def person = new Person(email: "email@test.com", password: "password").save()
        new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        new Scenario(person: person, name: "test1", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        new Scenario(person: person, name: "test2", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof Scenario
    }

    void "read returns instance"() {
        setupData()

        expect: "valid instance"
        service.read(1) instanceof Scenario
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }

    void "list max args param returns correct value"() {
        setupData()

        expect:
        service.list(max: 1).size() == 1
        service.list(max: 2).size() == 2
        service.list().size() == 3
        service.list(offset: 1).size() == 2
    }

    void "count returns number of scenarios"() {
        setupData()

        expect:
        service.count() == 3
    }

    void "delete with valid id deletes instance"() {
        given:
        def project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        def person = new Person(email: "email@test.com", password: "password").save()
        def scn = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save(flush: true)

        expect:
        service.get(scn.id) != null

        when:
        service.delete(scn.id)
        currentSession.flush()

        then:
        service.get(scn.id) == null
    }

    void "save with valid scenario returns instance"() {
        given:
        def project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        def person = new Person(email: "email@test.com", password: "password").save()
        def scn = new Scenario(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project)

        when:
        def saved = service.save(scn)

        then:
        saved instanceof Scenario
    }

    void "save with invalid scenario throws validation exception"() {
        given:
        def scn = new Scenario(name: "test", description: "desc",
                executionMethod: "Automated")

        when:
        service.save(scn)

        then:
        thrown(ValidationException)
    }
}
