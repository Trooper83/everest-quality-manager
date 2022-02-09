package com.everlution.test.service

import com.everlution.Person
import com.everlution.Project
import com.everlution.Step
import com.everlution.TestCase
import com.everlution.TestIteration
import com.everlution.TestIterationService
import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import spock.lang.Specification

@Integration
@Rollback
class TestIterationServiceSpec extends Specification {

    TestIterationService testIterationService

    void "test get"() {
        setup:
        def person = new Person(email: "test@test.com", password: "test").save()
        def project = new Project(name: "tc domain projectrdtrd", code: "tyu").save()
        def testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
        def id = new TestIteration(name: "name", testCase: testCase, steps: [], result: "ToDo").save().id

        expect:
        testIterationService.get(id) != null
    }

    void "create iteration from test case"() {
        given:
        def person = new Person(email: "test@test.com", password: "test").save()
        def project = new Project(name: "tc domain project", code: "tdp").save()
        def step = new Step(action: "action", result: "result")
        def testCase = new TestCase(person: person, name: "First Test Case", project: project, steps: [step]).save()

        when:
        def iteration = testIterationService.createIterations([testCase])

        then:
        iteration.first() instanceof TestIteration
    }
}
