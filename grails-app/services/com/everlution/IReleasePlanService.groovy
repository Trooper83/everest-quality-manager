package com.everlution

interface IReleasePlanService {

    ReleasePlan get(Serializable id)

    List<ReleasePlan> list(Map args)

    Long count()

    void delete(Serializable id)

    ReleasePlan save(ReleasePlan releasePlan)

}