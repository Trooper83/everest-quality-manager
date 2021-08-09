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

    void "test steps order persists"() {
        when:
        def paramMap = [creator: "test", name: "case", description: "desc",
                executionMethod: "automated", type: "api",
                "steps[0]": [action: "act1", result: "res1"],
                "steps[1]": [action: "act2", result: "res2"],
                "steps[2]": [action: "act3", result: "res3"]
        ]
        TestCase testCase = new TestCase(paramMap).save()

        def tc = testCaseService.get(testCase.id)

        then:
        tc.steps[0].action == "act1"
        tc.steps[1].action == "act2"
        tc.steps[2].action == "act3"
    }
}
