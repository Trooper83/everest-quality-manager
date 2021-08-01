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
        // TODO: Populate valid domain instances and return a valid ID
        //new TestCase(...).save(flush: true, failOnError: true)
        //new TestCase(...).save(flush: true, failOnError: true)
        //TestCase testCase = new TestCase(...).save(flush: true, failOnError: true)
        //new TestCase(...).save(flush: true, failOnError: true)
        //new TestCase(...).save(flush: true, failOnError: true)
        assert false, "TODO: Provide a setupData() implementation for this generated test suite"
        //testCase.id
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
        assert false, "TODO: Verify the correct instances are returned"
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
        assert false, "TODO: Provide a valid instance to save"
        TestCase testCase = new TestCase()
        testCaseService.save(testCase)

        then:
        testCase.id != null
    }
}
