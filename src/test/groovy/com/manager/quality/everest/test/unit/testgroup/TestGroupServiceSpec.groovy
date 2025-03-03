package com.manager.quality.everest.test.unit.testgroup

import com.manager.quality.everest.domains.Person
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.domains.TestCase
import com.manager.quality.everest.domains.TestGroup
import com.manager.quality.everest.services.testgroup.TestGroupService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification

class TestGroupServiceSpec extends Specification implements ServiceUnitTest<TestGroupService>, DataTest {

    @Shared Project project

    def setupSpec() {
        mockDomain(TestGroup)
    }

    def setup() {
        project = new Project(name: "Test Group Project", code: "TGP").save()
    }

    def setupData() {
        new TestGroup(name: "test group 1", project: project).save()
        new TestGroup(name: "test group 2", project: project).save()
        new TestGroup(name: "test group 3", project: project).save()
    }

    void "get with valid id returns instance"() {
        setup:
        setupData()

        expect:
        service.get(1) instanceof TestGroup
    }

    void "get with invalid id returns null"() {
        setup:
        setupData()

        expect:
        service.get(11111111111) == null
    }

    void "read with valid id returns instance"() {
        setup:
        setupData()

        expect:
        service.read(1) instanceof TestGroup
    }

    void "read with invalid id returns null"() {
        setup:
        setupData()

        expect:
        service.read(11111111111) == null
    }

    void "delete with valid id deletes instance"() {
        given:
        def id = new TestGroup(name: "name2", project: project).save(flush: true).id

        expect:
        service.get(id) != null

        when:
        service.delete(id)

        then:
        service.get(id) == null
    }

    void "delete with invalid id does not throw exception"() {
        when:
        service.delete(null)

        then:
        noExceptionThrown()
    }

    void "delete with test case"() {
        given:
        def person = new Person(email: "test1@test.com", password: "password").save()
        TestCase testCase = new TestCase(person: person, name: "first", description: "desc1",
                executionMethod: "Automated", type: "API", project: project).save()
        def group = new TestGroup(name: "name2", project: project).save(flush: true)
        group.addToTestCases(testCase)

        expect:
        service.get(group.id) != null

        when:
        service.delete(group.id)

        then:
        service.get(group.id) == null
    }

    void "delete with invalid id"() {
        when:
        service.delete(99999999999999)

        then:
        noExceptionThrown()
    }

    void "save with valid group returns instance"() {
        given:
        def group = new TestGroup(name: "name", project: project)

        when:
        def saved = service.save(group)

        then:
        saved instanceof TestGroup
    }

    void "save with invalid group throws validation exception"() {
        given:
        def group = new TestGroup()

        when:
        service.save(group)

        then:
        thrown(ValidationException)
    }

    void "get all returns list of groups"() {
        given:
        def tg1 = new TestGroup(name: "name", project: project).save()
        def tg2 = new TestGroup(name: "name1", project: project).save()
        def tg3 = new TestGroup(name: "name2", project: project).save(flush: true)

        when:
        def groups = service.getAll([tg1.id, tg2.id, tg3.id])

        then:
        groups.size() == 3
    }

    void "get all with invalid ids returns null values with valid values"() {
        given:
        def tg1 = new TestGroup(name: "name", project: project).save()
        def tg2 = new TestGroup(name: "name1", project: project).save()
        def tg3 = new TestGroup(name: "name2", project: project).save(flush: true)

        when:
        def groups = service.getAll([tg1.id, tg2.id, 99999999, tg3.id])

        then:
        groups.size() == 4
        groups.get(2) == null
        groups.get(0) != null
    }

    void "find all by project returns test groups"() {
        given:
        setupData()

        when:
        def results = service.findAllByProject(project, [:])

        then:
        results instanceof SearchResult
        results.count == 3
    }

    void "find all by project only returns groups with project"() {
        given:
        setupData()
        def proj = new Project(name: "BugServiceSpec Project1223", code: "BP8").save()
        def tg = new TestGroup(name: "name2", project: proj).save(flush: true)

        expect:
        TestGroup.list().contains(tg)

        when:
        def groups = service.findAllByProject(project, [:])

        then:
        groups.results.every { it.project.id == project.id }
        groups.count > 0
        groups.count == groups.results.size()
        !groups.results.contains(tg)
    }

    void "find all by project with null project id returns empty list"() {
        when:
        def groups = service.findAllByProject(null, [:])

        then:
        noExceptionThrown()
        groups.results.size() == 0
        groups.results.size() == groups.count
    }

    void "find all by name ilike returns test groups"(String q) {
        setup:
        setupData()

        expect:
        def results = service.findAllInProjectByName(project, q, [:])
        results.results.first().name == "test group 1"

        where:
        q << ['1', 'test group 1', 'oup 1', 'TEST GROUP 1']
    }

    void "find all in project by name only returns groups in project"() {
        given:
        setupData()
        def proj = new Project(name: "BugServiceSpec Project1223", code: "BP8").save()
        def tg = new TestGroup(name: "name2", project: proj).save(flush: true)

        expect:
        TestGroup.list().contains(tg)

        when:
        def groups = service.findAllInProjectByName(project, 'test', [:])

        then:
        groups.results.every { it.project.id == project.id }
        groups.results.size() > 0
        groups.results.size() == groups.count
        !groups.results.contains(tg)
    }

    void "find all in project by name with null project"() {
        given:
        setupData()

        when:
        def groups = service.findAllInProjectByName(null, 'nothing', [:])

        then:
        groups.results.empty
        groups.count == 0
        noExceptionThrown()
    }

    void "find all by name ilike with string"(String s, int size) {
        setup:
        setupData()

        expect:
        def groups = service.findAllInProjectByName(project, s, [:])
        groups.results.size() == size
        groups.results.size() == groups.count

        where:
        s           | size
        null        | 0
        ''          | 3
        'not found' | 0
        'test'      | 3
    }

    void "getWithPaginatedTests param values"(String max, String offset) {
        when:
        new TestGroup(name: "test group 1", project: project, testCases: [new TestCase()]).save()
        def params = ['max':max, 'offset':offset]
        service.getWithPaginatedTests(1, params)

        then:
        noExceptionThrown()

        where:
        max | offset
        '0'   | '0'
        '1'   | '0'
        '50'  | '0'
        '101' | '0'
        '-1'  | '0'
        null  | '0'
        null  | null
        '10'  | null
        '10'  | '1'
        '10'  | '-1'
        '10'  | '100'
        '100' | '100'
    }

    void "getWithPaginatedTests returns null group when not found"() {
        when:
        def params = ['max': '1', 'offset': '1']
        def group = service.getWithPaginatedTests(111, params)

        then:
        !group.testGroup
    }

    void "getWithPaginatedTests returns empty list group when not found"() {
        when:
        def params = ['max': '1', 'offset': '1']
        def group = service.getWithPaginatedTests(111, params)

        then:
        group.tests.empty
    }
}
