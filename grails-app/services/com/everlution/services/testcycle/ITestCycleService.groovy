package com.everlution.services.testcycle

import com.everlution.domains.TestCycle

interface ITestCycleService {

    TestCycle get(Serializable id)
}