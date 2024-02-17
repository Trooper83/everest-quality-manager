package com.everlution

interface ITestResultService {

    List<TestResult> findAllByAutomatedTest(AutomatedTest automatedTest)
}