package com.everlution.services.scenario

import com.everlution.SearchResult
import com.everlution.domains.Project
import com.everlution.domains.Scenario
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
