package com.everlution

interface ITestResultService {

    void delete(Serializable id)

    List<TestResult> findAllByTestCase(TestCase testCase)

    TestResult save(TestResult testResult)
}