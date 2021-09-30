package com.everlution

import grails.gorm.services.Service

@Service(Step)
interface TestStepService {

    Step get(Serializable id)

    Long count()

    void delete(Serializable id)

}