package com.everlution.services.scenario

import com.everlution.domains.Project
import com.everlution.domains.Scenario

interface IScenarioService {

    Scenario get(Serializable id)

    int countByProject(Project project)

    void delete(Serializable id)

    Scenario read(Serializable id)

    Scenario save(Scenario scenario)

}