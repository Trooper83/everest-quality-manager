package com.everlution

interface ITestCaseService {

    TestCase get(Serializable id)

    int countByProject(Project project)

    void delete(Serializable id)

    TestCase read(Serializable id)

    TestCase save(TestCase testCase)

}