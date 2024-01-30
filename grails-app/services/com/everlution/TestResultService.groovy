package com.everlution

import grails.gorm.services.Service

@Service(TestResult)
abstract class TestResultService implements ITestRunService {

    AutomatedTestService automatedTestService

    List<TestResult> createAndSave(Project project, List<TestRunResult> results) {
        List testResults = []
        results.forEach( r -> {
            def test = automatedTestService.findOrSave(project, r.testName)
            def tr = new TestResult(automatedTest: test, result: r.result)
            testResults.add(save(tr))
        })
        return testResults
    }
}
