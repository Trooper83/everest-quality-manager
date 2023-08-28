package com.everlution

interface IScenarioService {

    Scenario get(Serializable id)

    int countByProject(Project project)

    void delete(Serializable id)

    Scenario read(Serializable id)

    Scenario save(Scenario scenario)

}