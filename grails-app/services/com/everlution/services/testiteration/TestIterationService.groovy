package com.everlution.services.testiteration

import com.everlution.domains.TestCycle
import com.everlution.domains.TestIteration
import grails.gorm.services.Service

@Service(TestIteration)
interface TestIterationService {

    List<TestIteration> findAllByTestCycle(TestCycle cycle, Map args)

    TestIteration get(Serializable id)

    TestIteration read(Serializable id)

    TestIteration save(TestIteration testIteration)
}