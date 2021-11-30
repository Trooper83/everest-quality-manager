package com.everlution

import grails.gorm.services.Service

@Service(Scenario)
interface ScenarioService {

    Scenario get(Serializable id)

    List<Scenario> list(Map args)

    Long count()

    void delete(Serializable id)

    Scenario save(Scenario scenario)

}