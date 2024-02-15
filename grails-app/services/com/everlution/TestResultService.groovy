package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional
import grails.validation.ValidationException

@Service(TestResult)
abstract class TestResultService implements ITestResultService {

    AutomatedTestService automatedTestService

    @Transactional
    List<TestResult> createAndSave(Project project, List<TestRunResult> results) throws ValidationException {
        List<TestResult> testResults = []
        List<AutomatedTest> tests = []
        for(TestRunResult r in results) {
            def test = tests.find { it -> it.fullName == r.testName }
            if(test == null) {
                test = automatedTestService.findOrSave(project, r.testName)
                tests.add(test)
            }
            def tr = new TestResult(automatedTest: test, result: r.result, failureCause: r.failureCause)
            testResults.add(save(tr))
        }
        return testResults
    }
}
