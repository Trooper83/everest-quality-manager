package com.manager.quality.everest.services.scenario

import com.manager.quality.everest.SearchResult
import com.manager.quality.everest.domains.Project
import com.manager.quality.everest.domains.Scenario
import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Scenario)
abstract class ScenarioService implements IScenarioService {

    /**
     * finds all scenarios in the project
     */
    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        List<Scenario> scenarios = Scenario.findAllByProject(project, args)
        int c = Scenario.countByProject(project)
        return new SearchResult(scenarios, c)
    }

    /**
     * finds all scenarios in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map args) {
        List<Scenario> scenarios = Scenario.findAllByProjectAndNameIlike(project, "%${s}%", args)
        int c = Scenario.countByProjectAndNameIlike(project, "%${s}%")
        return new SearchResult(scenarios, c)
    }
}
