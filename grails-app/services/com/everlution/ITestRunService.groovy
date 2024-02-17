package com.everlution

interface ITestRunService {

    List<TestRun> findAllByProject(Project project)

    TestRun get(Serializable id)
}