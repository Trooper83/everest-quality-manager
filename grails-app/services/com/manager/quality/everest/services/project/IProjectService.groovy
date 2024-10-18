package com.manager.quality.everest.services.project

import com.manager.quality.everest.domains.Project

interface IProjectService {

    Project get(Serializable id)

    List<Project> list(Map args)

    Long count()

    void delete(Serializable id)

    Project read(Serializable id)

    Project save(Project project)

}