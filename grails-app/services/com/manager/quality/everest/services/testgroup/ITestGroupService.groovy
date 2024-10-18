package com.manager.quality.everest.services.testgroup

import com.manager.quality.everest.domains.TestGroup

interface ITestGroupService {

    TestGroup get(Serializable id)

    void delete(Serializable id)

    TestGroup read(Serializable id)

    TestGroup save(TestGroup testGroup)

}