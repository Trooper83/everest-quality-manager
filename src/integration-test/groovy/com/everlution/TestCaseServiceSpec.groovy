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
        TestCase testCase = new TestCase(name: "first", description: "desc1").save(flush: true, failOnError: true)
        new TestCase(name: "second", description: "desc2").save(flush: true, failOnError: true)
        new TestCase(name: "third", description: "desc3").save(flush: true, failOnError: true)
        new TestCase(name: "fourth", description: "desc4").save(flush: true, failOnError: true)
        new TestCase(name: "fifth", description: "desc5").save(flush: true, failOnError: true)
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
        TestCase testCase = new TestCase(name: "test", description: "desc")
        testCaseService.save(testCase)

        then:
        testCase.id != null
    }

    void "test save with steps"() {
        when:
        TestStep testStep = new TestStep(action: "do something", result: "something happened").save()
        TestStep testStep1 = new TestStep(action: "do something", result: "something happened").save()
        TestCase testCase = new TestCase(name: "test", description: "desc", steps: [testStep, testStep1])
        testCaseService.save(testCase)

        then:
        testCase.id != null
        testCase.steps.size() == 2
    }

    void "test date created auto generated"() {
        when:
        TestCase testCase = new TestCase(name: "test", description: "desc")
        testCaseService.save(testCase)

        then:
        testCaseService.get(testCase.id).dateCreated != null
    }
}
