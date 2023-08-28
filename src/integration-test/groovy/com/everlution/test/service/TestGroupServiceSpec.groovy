package com.everlution.test.service

import com.everlution.Person
import com.everlution.Project
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestCaseService
import com.everlution.TestGroup
import com.everlution.TestGroupService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Shared
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestGroupServiceSpec extends Specification {

    @Shared Project project

    TestCaseService testCaseService
    TestGroupService testGroupService
    SessionFactory sessionFactory

    private Long setupData() {
        project = new Project(name: "group service project", code: "gsp").save()
        new TestGroup(name: "name 1", project: project).save(flush: true, failOnError: true)
        new TestGroup(name: "name 12", project: project).save(flush: true, failOnError: true)
        TestGroup testGroup = new TestGroup(name: "name 123", project: project).save(flush: true, failOnError: true)
        new TestGroup(name: "name 1234", project: project).save(flush: true, failOnError: true)
        new TestGroup(name: "name 12345", project: project).save(flush: true, failOnError: true)
        testGroup.id
    }

    void "get returns instance"() {
        setupData()

        expect:
        testGroupService.get(1) instanceof TestGroup
    }

    void "read returns instance"() {
        setupData()

        expect:
        testGroupService.read(1) instanceof TestGroup
    }

    void "get with invalid id returns null"() {
        expect:
        testGroupService.get(999999) == null
    }

    void "read with invalid id returns null"() {
        expect:
        testGroupService.read(999999) == null
    }

    void "get all returns instances"() {
        setupData()

        when:
        def groups = testGroupService.getAll([1,2,3])

        then:
        groups.size() == 3
    }

    void "test delete"() {
        Long testGroupId = setupData()

        expect:
        TestGroup.findById(testGroupId) != null

        when:
        testGroupService.delete(testGroupId)
        sessionFactory.currentSession.flush()

        then:
        TestGroup.findById(testGroupId) == null
    }

    void "test save"() {
        given:
        setupData()

        when:
        TestGroup testGroup = new TestGroup(name: "name 21", project: project)
        testGroupService.save(testGroup)

        then:
        testGroup.id != null
    }

    void "save with invalid group throws validation exception"() {
        when:
        def group = new TestGroup()
        testGroupService.save(group)

        then:
        thrown(ValidationException)
    }

    void "delete test group with case"() {
        given:
        setupData()
        def group = new TestGroup(name: "test group", project: project).save()
        def person = new Person(email: "test1@test.com", password: "!Password2022").save()
        def testCase = new TestCase(person: person, name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI", project: project).save()
        group.addToTestCases(testCase)

        expect:
        group.testCases.size() == 1

        when:
        testGroupService.delete(group.id)
        sessionFactory.currentSession.flush()

        then:
        testGroupService.get(group.id) == null
        testCaseService.get(testCase.id) != null
    }

    void "find all by project returns only groups with project"() {
        given:
        setupData()

        when:
        def groups = testGroupService.findAllByProject(project, [:])

        then:
        groups.results.size() > 0
        groups.results.every { it.project.id == project.id }
    }

    void "find all by project with null project returns empty list"() {
        given:
        setupData()

        when:
        def groups = testGroupService.findAllByProject(null, [:])

        then:
        groups.results.size() == 0
        noExceptionThrown()
    }

    void "find all in project by name returns test groups"() {
        setup:
        setupData()

        expect:
        def groups = testGroupService.findAllInProjectByName(project, "name", [:])
        groups.results.first().name == "name 1"
    }
}
