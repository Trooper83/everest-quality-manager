package com.everlution

interface ITestCycleService {

    TestCycle get(Serializable id)

    TestCycle save(TestCycle testCycle)

}