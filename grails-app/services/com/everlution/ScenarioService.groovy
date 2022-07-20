package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Scenario)
abstract class ScenarioService implements IScenarioService {

    /**
     * gets all scenarios in the domain with the associated project
     * @param projectId - id of the project
     * @return - list of all scenarios with the project
     */
    @Transactional
    List<Scenario> findAllByProject(Project project) {
        return Scenario.findAllByProject(project)
    }

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
