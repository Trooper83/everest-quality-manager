package com.everlution

import grails.gorm.services.Service

@Service(TestIteration)
interface TestIterationService {

    TestIteration get(Serializable id)
}