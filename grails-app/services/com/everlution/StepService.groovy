package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(Step)
abstract class StepService implements IStepService {

    LinkService linkService

    /**
     * deletes a step, if any linked steps exist it deletes them first
     * @param id
     */
    @Transactional
    void delete(Serializable id) {
        def step = get(id)
        def links = linkService.getLinks(step.id, step.project)
        links.each {
            linkService.delete(it.id)
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


    /**
     * gets all related steps
     */
    Map<String, List<Step>> getLinkedStepsByRelation(Step step) {
        if (!step) {
            return [children: [], parents: [], siblings: []]
        }

        List<Link> links = linkService.getLinks(step.id, step.project)
        links.removeAll { it -> it.ownerId != step.id }

        List<Step> children = [], parents = [], siblings = []

        def childLinks = links.findAll { it -> it.relation == Relationship.IS_PARENT_OF.name }
        childLinks.each { it -> children.add(get(it.linkedId)) }

        def parentLinks = links.findAll { it -> it.relation == Relationship.IS_CHILD_OF.name }
        parentLinks.each { it -> parents.add(get(it.linkedId)) }

        def siblingLinks = links.findAll { it -> it.relation == Relationship.IS_SIBLING_OF.name }
        siblingLinks.each { it -> siblings.add(get(it.linkedId)) }

        return [children: children, parents: parents, siblings: siblings]
    }
}
