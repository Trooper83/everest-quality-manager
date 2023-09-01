package com.everlution

import grails.gorm.services.Service

@Service(Link)
abstract class LinkService implements ILinkService {

   /* StepService stepService

    @Transactional
    void createStepLinks(Step step, List<Link> links) {

    }

    private StepLink createInversion(Step step, Link link) {
        def l
        switch (link.relation) {
            case Relationship.IS_PARENT_OF.name:
                l = new StepLink(relation: Relationship.IS_CHILD_OF.name, owner: stepService.read(link.id),
                        linkedStep: step, project: step.project)
                break
            case Relationship.IS_CHILD_OF.name:
                l = new StepLink(relation: Relationship.IS_PARENT_OF.name, owner: stepService.read(link.id),
                        linkedStep: step, project: step.project)
                break
            default: //sibling
                l = new StepLink(relation: Relationship.IS_SIBLING_OF.name, owner: stepService.read(link.id),
                        linkedStep: step, project: step.project)
        }
        return l
    }*/

    /**
     * gets all related steps
     * @param step - the step to find relations for
     * @return a map containing all related steps
     */
    List<Link> getStepLinks(Step step) {
        if (!step) {
            return []
        }
        def c = Link.createCriteria()
        def links = c {
            eq("project", step.project)
            or {
                eq("ownerId", step.id)
                eq("linkedId", step.id)
            }
        }
        return links
    }

    /**
     * gets all related steps
     * @param step - the step to find relations for
     * @return a map containing all related steps by type
     */
    Map<String, List<Link>> getStepLinksByType(Step step) {
        if (!step) {
            return [:]
        }
        //TODO: could we just use one query to get all by owner and then filter the collection?
        List<Link> children = Link.findAllByProjectAndRelationAndOwnerId(step.project, Relationship.IS_PARENT_OF.name, step.id)
        List<Link> parents = Link.findAllByProjectAndRelationAndOwnerId(step.project, Relationship.IS_CHILD_OF.name, step.id)
        List<Link> siblings = Link.findAllByProjectAndRelationAndOwnerId(step.project, Relationship.IS_SIBLING_OF.name, step.id)
        return ['children':children, 'parents': parents, 'siblings':siblings]
    }
}
