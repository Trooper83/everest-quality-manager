package com.everlution

import grails.gorm.services.Service

@Service(ReleasePlan)
interface ReleasePlanService {

    ReleasePlan get(Serializable id)

    List<ReleasePlan> list(Map args)

    Long count()

    void delete(Serializable id)

    ReleasePlan save(ReleasePlan releasePlan)

}