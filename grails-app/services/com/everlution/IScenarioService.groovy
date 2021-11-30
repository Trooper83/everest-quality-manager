package com.everlution

interface IScenarioService {

    Scenario get(Serializable id)

    List<Scenario> list(Map args)

    Long count()

    void delete(Serializable id)

    Scenario save(Scenario scenario)

}