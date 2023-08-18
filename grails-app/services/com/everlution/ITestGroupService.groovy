package com.everlution

interface ITestGroupService {

    TestGroup get(Serializable id)

    void delete(Serializable id)

    TestGroup read(Serializable id)

    TestGroup save(TestGroup testGroup)

}