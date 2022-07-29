package com.everlution

interface IScenarioService {

    List<Scenario> findAllByProject(Project project)

    Scenario get(Serializable id)

    List<Scenario> list(Map args)

    Long count()

    void delete(Serializable id)

    Scenario read(Serializable id)

    Scenario save(Scenario scenario)

}