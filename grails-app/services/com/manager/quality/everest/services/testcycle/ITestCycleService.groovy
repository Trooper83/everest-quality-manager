package com.manager.quality.everest.services.testcycle

import com.manager.quality.everest.domains.TestCycle

interface ITestCycleService {

    TestCycle get(Serializable id)
}