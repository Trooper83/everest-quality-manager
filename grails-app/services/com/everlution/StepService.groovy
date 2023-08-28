package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Step)
abstract class StepService implements IStepService {

    StepLinkService stepLinkService

    /**
     * deletes a step, if any linked steps exist it deletes them first
     * @param id
     */
    @Transactional
    void delete(Serializable id) {
        def step = get(id)
        def links = stepLinkService.getStepLinks(step)
        links.each {
            stepLinkService.delete(it.id)
        }
        step.delete()
    }

    /**
     * finds all steps in a project
     */
    @Transactional
    SearchResult findAllByProject(Project project, Map args) {
        int c = Step.countByProjectAndIsBuilderStep(project, true)
        List steps = Step.findAllByProjectAndIsBuilderStep(project, true, args)
        return new SearchResult(steps, c)
    }

    /**
     * finds all steps in the project with a name
     * that contains the string
     * @param name - the string to search
     */
    @Transactional
    SearchResult findAllInProjectByName(Project project, String s, Map args) {
        List steps = Step.findAllByProjectAndNameIlikeAndIsBuilderStep(project, "%${s}%", true, args)
        int c = Step.countByProjectAndNameIlikeAndIsBuilderStep(project, "%${s}%", true)
        return new SearchResult(steps, c)
    }
}
