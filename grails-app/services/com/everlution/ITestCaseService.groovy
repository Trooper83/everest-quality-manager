package com.everlution

interface ITestCaseService {

    TestCase get(Serializable id)

    int countByProject(Project project)

    TestCase read(Serializable id)

    TestCase save(TestCase testCase)

}