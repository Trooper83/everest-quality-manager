package com.everlution

import grails.gorm.services.Service

@Service(TestResult)
abstract class TestResultService implements ITestResultService {

    void deleteAllByTestCase(TestCase testCase) {
        def results = findAllByTestCase(testCase)
        results.forEach(r -> delete(r.id))
    }
}
