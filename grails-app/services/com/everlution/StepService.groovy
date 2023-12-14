package com.everlution

import grails.gorm.services.Service

@Service(Step)
interface StepService {

    List<Step> findAllByTemplate(StepTemplate template)

    Step get(Serializable id)

    Step read(Serializable id)

    Step save(Step step)
}