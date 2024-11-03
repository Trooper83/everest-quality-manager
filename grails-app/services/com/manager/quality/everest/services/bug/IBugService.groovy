package com.manager.quality.everest.services.bug

import com.manager.quality.everest.domains.Bug
import com.manager.quality.everest.domains.Project

interface IBugService {

    void delete(Serializable id)

    Bug get(Serializable id)

    int countByProjectAndStatus(Project project, String status)

    Bug read(Serializable id)

    Bug save(Bug bug)

}