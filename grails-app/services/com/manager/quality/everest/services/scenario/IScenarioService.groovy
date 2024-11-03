package com.manager.quality.everest.services.scenario

import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.Scenario

interface IScenarioService {

    Scenario get(Serializable id)

    int countByProject(Project project)

    void delete(Serializable id)

    Scenario read(Serializable id)

    Scenario save(Scenario scenario)

}