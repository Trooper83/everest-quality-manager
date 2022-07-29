package com.everlution

interface ITestCaseService {

    List<TestCase> findAllByProject(Project project)

    TestCase get(Serializable id)

    List<TestCase> list(Map args)

    Long count()

    void delete(Serializable id)

    TestCase read(Serializable id)

    TestCase save(TestCase testCase)

}