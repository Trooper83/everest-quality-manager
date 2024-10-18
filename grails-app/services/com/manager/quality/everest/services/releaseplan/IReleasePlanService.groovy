package com.manager.quality.everest.services.releaseplan

import com.manager.quality.everest.domains.ReleasePlan

interface IReleasePlanService {

    ReleasePlan get(Serializable id)

    void delete(Serializable id)

    ReleasePlan read(Serializable id)

    ReleasePlan save(ReleasePlan releasePlan)

}