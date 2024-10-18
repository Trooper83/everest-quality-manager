package com.manager.quality.everest.test.unit.testcase

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.services.testcase.TestCaseService
import com.manager.quality.everest.domains.TestResult
import com.manager.quality.everest.controllers.command.RemovedItems
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class TestCaseServiceSpec extends Specification implements ServiceUnitTest<TestCaseService>, DataTest {

    @Shared Person person
    @Shared Project project

    def setupSpec() {
        mockDomains(TestCase, Person, Project, TestResult)
    }

    def setup() {
        project = new Project(name: "Unit Test Project For Service", code: "BPZ").save()
        person = new Person(email: "email@test.com", password: "!Password2022").save()
    }

    private void setupData() {
        new TestCase(person: person, name: "first test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(person: person, name: "second test", description: "desc",
                executionMethod: "Automated", type: "API", project: project).save()
        new TestCase(person: person, name: "third test", description: "desc",
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

    void "delete with invalid id"() {
        when:
        service.delete(999)

        then:
        noExceptionThrown()
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

    void "find all by project returns test cases"() {
        when:
        def tests = service.findAllByProject(project, [:])

        then:
        tests.results instanceof List<TestCase>
    }

    void "find all by project only returns test cases with project"() {
        given:
        def proj = new Project(name: "BugServiceSpec Project1223", code: "BP8").save()
        def tc = new TestCase(person: person, name: "test", description: "desc",
                executionMethod: "Automated", type: "API", project: proj).save(flush: true)

        expect:
        TestCase.list().contains(tc)

        when:
        def tests = service.findAllByProject(project, [:]).results

        then:
        tests.every { it.project.id == project.id }
        !tests.contains(tc)
    }

    void "find all by project with null project id returns empty list"() {
        when:
        def tests = service.findAllByProject(null, [:])

        then:
        noExceptionThrown()
        tests.results.size() == 0
    }

    void "find all by name ilike returns test case"(String q) {
        setup:
        setupData()

        expect:
        def tests = service.findAllInProjectByName(project, q, [:])
        tests.results.first().name == "first test"

        where:
        q << ['first', 'fi', 'irs', 't te', 'FIRST']
    }

    void "find all in project by name only returns tests in project"() {
        given:
        setupData()
        def proj = new Project(name: "TestService Spec Project1223", code: "BP8").save()
        def tc = new TestCase(person: person, name: "test999999999", description: "desc",
                executionMethod: "Automated", type: "API", project: proj).save(flush: true)

        expect:
        TestCase.list().contains(tc)

        when:
        def tests = service.findAllInProjectByName(project, 'test', [:])

        then:
        tests.results.every { it.project.id == project.id }
        tests.results.size() > 0
        !tests.results.contains(tc)
    }

    void "find all in project by name with null project"() {
        given:
        setupData()

        when:
        def groups = service.findAllInProjectByName(null, 'test', [:])

        then:
        groups.results.empty
        noExceptionThrown()
    }

    void "find all by name ilike with string"(String s, int size) {
        setup:
        setupData()

        expect:
        def tests = service.findAllInProjectByName(project, s, [:])
        tests.results.size() == size
        tests.count == size

        where:
        s           | size
        null        | 0
        ''          | 3
        'not found' | 0
        'test'      | 3
    }

    void "getAllByGroup returns empty list when group null"() {
        when:
        def t = service.getAllByGroup(null, [:])

        then:
        t.empty
    }

    void "getAllByGroup returns empty list when group not found"() {
        when:
        def t = service.getAllByGroup(999, [:])

        then:
        t.empty
    }
}
