package com.everlution

interface IReleasePlanService {

    ReleasePlan get(Serializable id)

    void delete(Serializable id)

    ReleasePlan read(Serializable id)

    ReleasePlan save(ReleasePlan releasePlan)

}