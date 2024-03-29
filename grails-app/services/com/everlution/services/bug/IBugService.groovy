package com.everlution.services.bug

import com.everlution.domains.Bug
import com.everlution.domains.Project

interface IBugService {

    void delete(Serializable id)

    Bug get(Serializable id)

    int countByProject(Project project)

    Bug read(Serializable id)

    Bug save(Bug bug)

}