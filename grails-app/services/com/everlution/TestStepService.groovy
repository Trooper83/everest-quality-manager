package com.everlution

import grails.gorm.services.Service

@Service(TestStep)
interface TestStepService {

    TestStep get(Serializable id)

    Long count()

    void delete(Serializable id)

}