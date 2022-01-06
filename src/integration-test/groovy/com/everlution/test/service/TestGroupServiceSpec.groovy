package com.everlution.test.service

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

    TestGroupService testGroupService
    SessionFactory sessionFactory

    private Long setupData() {
        new TestGroup(name: "name 1").save(flush: true, failOnError: true)
        new TestGroup(name: "name 12").save(flush: true, failOnError: true)
        TestGroup testGroup = new TestGroup(name: "name 123").save(flush: true, failOnError: true)
        new TestGroup(name: "name 1234").save(flush: true, failOnError: true)
        new TestGroup(name: "name 12345").save(flush: true, failOnError: true)
        testGroup.id
    }

    void "get returns instance"() {
        setupData()

        expect:
        testGroupService.get(1) instanceof TestGroup
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
        testGroupService.count() == 5
    }

    void "test delete"() {
        Long testGroupId = setupData()

        expect:
        testGroupService.count() == 5

        when:
        testGroupService.delete(testGroupId)
        sessionFactory.currentSession.flush()

        then:
        testGroupService.count() == 4
    }

    void "test save"() {
        when:
        TestGroup testGroup = new TestGroup(name: "name 21")
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
}
