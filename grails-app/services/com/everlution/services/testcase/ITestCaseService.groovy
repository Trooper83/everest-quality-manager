package com.everlution.services.testcase

import com.everlution.domains.Project
import com.everlution.domains.TestCase

interface ITestCaseService {

    void delete(Serializable id)

    TestCase get(Serializable id)

    int countByProject(Project project)

    TestCase read(Serializable id)

    TestCase save(TestCase testCase)

}