package com.everlution.services.testgroup

import com.everlution.domains.TestGroup

interface ITestGroupService {

    TestGroup get(Serializable id)

    void delete(Serializable id)

    TestGroup read(Serializable id)

    TestGroup save(TestGroup testGroup)

}