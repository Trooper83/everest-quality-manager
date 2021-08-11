package com.everlution

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
                executionMethod: "automated", type: "api").save(flush: true, failOnError: true)
        new TestCase(creator: "test",name: "second", description: "desc2",
                executionMethod: "automated", type: "ui").save(flush: true, failOnError: true)
        new TestCase(creator: "test",name: "third", description: "desc3",
                executionMethod: "automated", type: "api").save(flush: true, failOnError: true)
        new TestCase(creator: "test",name: "fourth", description: "desc4",
                executionMethod: "manual", type: "ui").save(flush: true, failOnError: true)
        new TestCase(creator: "test",name: "fifth", description: "desc5",
                executionMethod: "automated", type: "api").save(flush: true, failOnError: true)
        testCase.id
    }

    void "test get"() {
        setupData()

        expect:
        testCaseService.get(1) != null
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
        testCaseService.count() == 5
    }

    void "test delete"() {
        Long testCaseId = setupData()

        expect:
        testCaseService.count() == 5

        when:
        testCaseService.delete(testCaseId)
        sessionFactory.currentSession.flush()

        then:
        testCaseService.count() == 4
    }

    void "test save"() {
        when:
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "automated", type: "api",)
        testCaseService.save(testCase)

        then:
        testCase.id != null
    }

    void "test date created auto generated"() {
        when:
        TestCase testCase = new TestCase(creator: "test", name: "test", description: "desc",
                executionMethod: "automated", type: "api",)
        testCaseService.save(testCase)

        then:
        testCaseService.get(testCase.id).dateCreated != null
    }
}
