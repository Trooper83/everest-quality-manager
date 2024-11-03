package com.manager.quality.everest.services.testcase

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.TestCase

interface ITestCaseService {

    void delete(Serializable id)

    TestCase get(Serializable id)

    int countByProject(Project project)

    TestCase read(Serializable id)

    TestCase save(TestCase testCase)

}