package com.manager.quality.everest.services.testresult

import com.manager.quality.everest.domains.AutomatedTest
import com.manager.quality.everest.domains.TestResult
import com.manager.quality.everest.domains.TestRun

interface ITestResultService {

    List<TestResult> findAllByAutomatedTest(AutomatedTest automatedTest)

    List<TestResult> findAllByTestRun(TestRun testRun, Map args)
}