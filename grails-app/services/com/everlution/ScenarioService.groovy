package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Scenario)
abstract class ScenarioService implements IScenarioService {

    @Transactional
    void deleteAllScenariosByProject(Project project) {
        def scenarios = Scenario.findAllByProject(project)
        scenarios.each {
            delete(it.id)
        }
    }
}
