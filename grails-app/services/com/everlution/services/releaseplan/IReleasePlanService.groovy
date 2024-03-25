package com.everlution.services.releaseplan

import com.everlution.domains.ReleasePlan

interface IReleasePlanService {

    ReleasePlan get(Serializable id)

    void delete(Serializable id)

    ReleasePlan read(Serializable id)

    ReleasePlan save(ReleasePlan releasePlan)

}