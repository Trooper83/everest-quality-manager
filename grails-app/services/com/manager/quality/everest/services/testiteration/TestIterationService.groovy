package com.manager.quality.everest.services.testiteration

import com.manager.quality.everest.domains.TestCycle
import com.manager.quality.everest.domains.TestIteration
import grails.gorm.services.Service

@Service(TestIteration)
interface TestIterationService {

    List<TestIteration> findAllByTestCycle(TestCycle cycle, Map args)

    TestIteration get(Serializable id)

    TestIteration read(Serializable id)

    TestIteration save(TestIteration testIteration)
}