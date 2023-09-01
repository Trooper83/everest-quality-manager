package com.everlution

import grails.gorm.services.Service
import grails.gorm.transactions.Transactional

@Service(StepLink)
abstract class StepLinkService implements IStepLinkService {

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
    List<StepLink> getStepLinks(Step step) {
        if (!step) {
            return []
        }
        def c = StepLink.createCriteria()
        def links = c {
            eq("project", step.project)
            or {
                eq("owner", step)
                eq("linkedStep", step)
            }
        }
        return links
    }

    /**
     * gets all related steps
     * @param step - the step to find relations for
     * @return a map containing all related steps by type
     */
    Map<String, List<StepLink>> getStepLinksByType(Step step) {
        if (!step) {
            return [:]
        }
        //TODO: could we just use one query to get all by owner and then filter the collection?
        List<StepLink> children = StepLink.findAllByProjectAndRelationAndOwner(step.project, Relationship.IS_PARENT_OF.name, step)
        List<StepLink> parents = StepLink.findAllByProjectAndRelationAndOwner(step.project, Relationship.IS_CHILD_OF.name, step)
        List<StepLink> siblings = StepLink.findAllByProjectAndRelationAndOwner(step.project, Relationship.IS_SIBLING_OF.name, step)
        return ['children':children, 'parents': parents, 'siblings':siblings]
    }
}
