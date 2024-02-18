package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

@Service(TestRun)
abstract class TestRunService implements ITestRunService {

    AutomatedTestService automatedTestService

    @Transactional
    TestRun save(TestRun testRun) {
        if(!testRun.validate()) {
            throw new ValidationException("TestRun failed to validate", testRun.errors)
        }
        testRun.save(flush: true)
    }

    @Transactional
    TestRun createAndSave(String name, Project project, List<TestRunResult> results) {
        List<TestResult> testResults = []
        List<AutomatedTest> tests = []
        for(TestRunResult r in results) {
            def test = tests.find { it -> it.fullName == r.testName }
            if(test == null) {
                test = automatedTestService.findOrSave(project, r.testName)
                tests.add(test)
            }
            def tr = new TestResult(automatedTest: test, result: r.result, failureCause: r.failureCause)
            testResults.add(tr)
        }
        save(new TestRun(name: name, project: project, testResults: testResults))
    }

    List<TestRun> findAllInProjectByName(Project project, String name, Map args) {
        return TestRun.findAllByProjectAndNameIlike(project, "%${name}%", args)
    }
}
