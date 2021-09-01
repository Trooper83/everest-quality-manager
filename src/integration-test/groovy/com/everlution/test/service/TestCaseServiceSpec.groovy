package com.everlution.test.service

import com.everlution.TestCase
import com.everlution.TestCaseService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TestCaseServiceSpec extends Specification {

    TestCaseService testCaseService
    SessionFactory sessionFactory

    private Long setupData() {
        TestCase testCase = new TestCase(creator: "test",name: "first", description: "desc1",
                executionMethod: "Automated", type: "API").save(flush: true, failOnError: true)
        new TestCase(creator: "test",name: "second", description: "desc2",
                executionMethod: "Automated", type: "UI").save(flush: true, failOnError: true)
        new TestCase(creator: "test",name: "third", description: "desc3",
                executionMethod: "Automated", type: "API").save(flush: true, failOnError: true)
        new TestCase(creator: "test",name: "fourth", description: "desc4",
                executionMethod: "Manual", type: "UI").save(flush: true, failOnError: true)
        testCase.id
    }

    void "test get"() {
        def id = setupData()

        expect:
        testCaseService.get(id) != null
    }

    void "test list"() {
        setupData()

        when:
        List<TestCase> testCaseList = testCaseService.list(max: 2, offset: 2)

        then:
        testCaseList.size() == 2
    }

    void "test count"() {
        setupData()

        expect:
        testCaseService.count() > 1
    }

    void "test delete"() {
        Long testCaseId = setupData()

        given:
        def count = testCaseService.count()

        when:
        testCaseService.delete(testCaseId)
        sessionFactory.currentSession.flush()

        then:
        testCaseService.count() == count - 1
    }

    void "test save"() {
        when:
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API",)
        testCaseService.save(testCase)

        then:
        testCase.id != null
    }

    void "test date created auto generated"() {
        when:
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "Automated", type: "API",)
        testCaseService.save(testCase)

        then:
        testCaseService.get(testCase.id).dateCreated != null
    }
}
