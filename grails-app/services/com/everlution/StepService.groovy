package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Step)
abstract class StepService implements IStepService {

    /**
     * finds all steps in a project
     */
    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        int c = Step.countByProject(project)
        List steps = Step.findAllByProject(project, args)
        return new SearchResult(steps, c)
    }

    /**
     * finds all steps in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map args) {
        List steps = Step.findAllByProjectAndNameIlike(project, "%${s}%", args)
        int c = Step.countByProjectAndNameIlike(project, "%${s}%")
        return new SearchResult(steps, c)
    }
}
