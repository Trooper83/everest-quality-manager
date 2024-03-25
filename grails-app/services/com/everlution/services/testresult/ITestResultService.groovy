package com.everlution.services.testresult

import com.everlution.domains.AutomatedTest
import com.everlution.domains.TestResult
import com.everlution.domains.TestRun

interface ITestResultService {

    List<TestResult> findAllByAutomatedTest(AutomatedTest automatedTest)

    List<TestResult> findAllByTestRun(TestRun testRun, Map args)
}