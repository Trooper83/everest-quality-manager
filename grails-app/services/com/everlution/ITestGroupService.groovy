package com.everlution

interface ITestGroupService {

    TestGroup get(Serializable id)

    List<TestGroup> list(Map args)

    Long count()

    void delete(Serializable id)

    TestGroup save(TestGroup testGroup)

}