package com.manager.quality.everest.services.testrun

import com.manager.quality.everest.domains.TestRun

interface ITestRunService {

    TestRun get(Serializable id)
}