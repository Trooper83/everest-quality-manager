package com.everlution.test.testiteration

import com.everlution.Person
import com.everlution.Project
import com.everlution.TestCase
import com.everlution.TestIteration
import com.everlution.TestIterationService
import grails.testing.gorm.DataTest
import grails.testing.services.ServiceUnitTest
import spock.lang.Shared
import spock.lang.Specification

class TestIterationServiceSpec extends Specification implements ServiceUnitTest<TestIterationService>, DataTest {

    @Shared Person person
    @Shared Project project
    @Shared TestCase testCase

    def setupSpec() {
        mockDomains(Person, Project, TestCase, TestIteration)
    }

    def setup() {
        person = new Person(email: "test@test.com", password: "test").save()
        project = new Project(name: "tc domain project", code: "tdp").save()
        testCase = new TestCase(person: person, name: "First Test Case", description: "test",
                executionMethod: "Manual", type: "UI", project: project).save()
    }

    void "get with valid id returns instance"() {
        when:
        new TestIteration(name: "name", testCase: testCase, steps: [], result: "ToDo").save()

        then:
        service.get(1) instanceof TestIteration
    }

    void "get with invalid id returns null"() {
        expect:
        service.get(999999) == null
    }
}
