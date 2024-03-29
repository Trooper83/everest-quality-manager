package com.everlution.services.testrun

import com.everlution.domains.TestRun

interface ITestRunService {

    TestRun get(Serializable id)
}