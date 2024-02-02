package com.everlution

interface ITestResultService {

    TestResult save(TestResult testResult)

    List<TestResult> findAllByAutomatedTest(AutomatedTest automatedTest)
}