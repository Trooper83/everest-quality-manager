package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Scenario)
abstract class ScenarioService implements IScenarioService {

    /**
     * finds all scenarios in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    List<Scenario> findAllInProjectByName(Project project, String s) {
        return Scenario.findAllByProjectAndNameIlike(project, "%${s}%")
    }
}
