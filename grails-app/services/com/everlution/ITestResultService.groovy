package com.everlution

interface ITestResultService {

    List<TestResult> findAllByAutomatedTest(AutomatedTest automatedTest)

    List<TestResult> findAllByTestRun(TestRun testRun, Map args)
}