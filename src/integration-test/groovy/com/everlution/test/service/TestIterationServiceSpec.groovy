package com.everlution.test.service

import com.everlution.PersonService
import com.everlution.ProjectService
import com.everlution.TestCase
import com.everlution.TestIteration
import com.everlution.TestIterationService
import grails.gorm.transactions.Rollback
import grails.testing.mixin.integration.Integration
import spock.lang.Specification

@Integration
@Rollback
class TestIterationServiceSpec extends Specification {

    PersonService personService
    ProjectService projectService
    TestIterationService testIterationService

    void "test get"() {
        def person = personService.list(max: 1).first()
        def project = projectService.list(max: 1).first()
        def testCase = new TestCase(name: "name of test case", project: project, person: person)
        def iteration = new TestIteration(name: "name of test iteration", testCase: testCase, result: "ToDo", steps: []).save()

        expect:
        testIterationService.get(iteration.id) != null
    }
}
