package com.everlution

interface IScenarioService {

    Scenario get(Serializable id)

    List<Scenario> list(Map args)

    Long count()

    int countByProject(Project project)

    void delete(Serializable id)

    Scenario read(Serializable id)

    Scenario save(Scenario scenario)

}