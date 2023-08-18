package com.everlution

import grails.gorm.services.Service

@Service(Step)
interface StepService {

    Step get(Serializable id)

    void delete(Serializable id)

    Step save(Step step)
}