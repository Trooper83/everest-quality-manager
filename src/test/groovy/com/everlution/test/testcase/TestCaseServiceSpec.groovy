package com.everlution.test.testcase

import com.everlution.Person
import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.command.RemovedItems
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class TestCaseServiceSpec extends Specification implements ServiceUnitTest<TestCaseService>, DataTest {

    @Shared Person person
    @Shared Project project

    def setupSpec() {
        mockDomains(TestCase, Person, Project)
    }

    def setup() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "password").save()
    }

    private void setupData() {
        new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(person: person, name: "test1", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(person: person, name: "test2", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save(flush: true)
    }

    void "get with valid id returns instance"() {
        setupData()

        expect: "valid instance"
        service.get(1) instanceof TestCase
    }

    void "read returns instance"() {
        setupData()

        expect: "valid instance"
        service.read(1) instanceof TestCase
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

    void "count returns number"() {
        setupData()

        expect:
        service.count() == 3
    }

    void "delete with valid id deletes instance"() {
        given:
        def tc = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save(flush: true)

        expect:
        service.get(tc.id) != null

        when:
        service.delete(tc.id)
        currentSession.flush()

        then:
        service.get(tc.id) == null
    }

    void "save with valid object returns instance"() {
        given:
        def tc = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project)

        when:
        def saved = service.save(tc)

        then:
        saved instanceof TestCase
    }

    void "save with invalid object throws validation exception"() {
        given:
        def tc = new TestCase(name: "test", description: "desc")

        when:
        service.save(tc)

        then:
        thrown(ValidationException)
    }

    void "saveUpdate with invalid object throws validation exception"() {
        given:
        def tc = new TestCase(name: "test", description: "desc")

        when:
        service.saveUpdate(tc, new RemovedItems())

        then:
        thrown(ValidationException)
    }

    void "saveUpdate returns valid instance"() {
        given:
        def tc = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: project)

        when:
        def saved = service.saveUpdate(tc, new RemovedItems())

        then:
        saved instanceof TestCase
    }

    void "get all returns list of tests"() {
        given:
        def tc1 = new TestCase(name: "name", project: project, person: person).save()
        def tc2 = new TestCase(name: "name1", project: project, person: person).save()
        def tc3 = new TestCase(name: "name2", project: project, person: person).save(flush: true)

        when:
        def tests = service.getAll([tc1.id, tc2.id, tc3.id])

        then:
        tests.size() == 3
    }

    void "get all with invalid ids returns null values with valid values"() {
        given:
        def tc1 = new TestCase(name: "name", project: project, person: person).save()
        def tc2 = new TestCase(name: "name1", project: project, person: person).save()
        def tc3 = new TestCase(name: "name2", project: project, person: person).save(flush: true)

        when:
        def tests = service.getAll([tc1.id, tc2.id, 99999999, tc3.id])

        then:
        tests.size() == 4
        tests.get(2) == null
        tests.get(0) != null
    }
}
