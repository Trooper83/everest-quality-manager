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
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestGroupServiceSpec extends Specification {

    ProjectService projectService
    TestCaseService testCaseService
    TestGroupService testGroupService
    SessionFactory sessionFactory

    private Long setupData() {
        def project = new Project(name: "group service project", code: "gsp").save()
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

    void "get all returns instances"() {
        setupData()

        when:
        def groups = testGroupService.getAll([1,2,3])

        then:
        groups.size() == 3
    }

    void "test list"() {
        setupData()

        when:
        List<TestGroup> testGroupList = testGroupService.list(max: 2, offset: 2)

        then:
        testGroupList.size() == 2
    }

    void "test count"() {
        setupData()

        expect:
        testGroupService.count() > 0
    }

    void "test delete"() {
        Long testGroupId = setupData()

        expect:
        testGroupService.count() == 7

        when:
        testGroupService.delete(testGroupId)
        sessionFactory.currentSession.flush()

        then:
        testGroupService.count() == 6
    }

    void "test save"() {
        when:
        def project = new Project(name: "group service project", code: "gsp").save()
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
        def project = projectService.list(max: 1).first()
        def group = new TestGroup(name: "test group", project: project).save()
        def person = new Person(email: "test1@test.com", password: "password").save()
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
}
