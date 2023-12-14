package com.everlution

interface ITestCaseService {

    void delete(Serializable id)

    TestCase get(Serializable id)

    int countByProject(Project project)

    TestCase read(Serializable id)

    TestCase save(TestCase testCase)

}